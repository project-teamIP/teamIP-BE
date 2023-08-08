package com.teamip.heyhello.domain.memo.service;

import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.memo.dto.MemoResponseDto;
import com.teamip.heyhello.domain.memo.entity.Memo;
import com.teamip.heyhello.domain.memo.repository.MemoRepository;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.domain.user.repository.UserRepository;
import com.teamip.heyhello.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 메모 작성 메서드
    public MemoResponseDto createMemo(String tokenValue, MemoRequestDto requestDto){
        // token에서 현재 user 조회
        User currentUser = getUserFromToken(tokenValue);

        // RequestDto -> Entity
        Memo memo = new Memo(currentUser,requestDto);

        // MemoRepository에 저장
        Memo saveMemo = memoRepository.save(memo);

        return new MemoResponseDto(saveMemo);
    }

    // 사용자가 작성한 메모 목록 조회 메서드
    public List<MemoResponseDto> getMemoList(String tokenValue){
        // token에서 현재 user 조회
        User currentUser = getUserFromToken(tokenValue);

        // 현재 유저의 ID 가져오기
        Long userId = currentUser.getId();

        // 현재 유저가 작성한 메모 목록 가져오기
        List<Memo> memoList = memoRepository.findByUserId(userId);
        
        return memoList.stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 사용자가 작성한 특정 메모 조회 메서드
    public MemoResponseDto getMemoById(String tokenValue, Long id){
        // token에서 현재 user 조회
        User currentUser = getUserFromToken(tokenValue);

        // 해당 메모 존재하는지 확인
        Memo memo = findMemo(id);

        // 작성자만 조회할 수 있도록 확인
        if(currentUser.getId() != memo.getUser().getId()){
            throw new IllegalArgumentException("해당 메모를 작성한 사용자가 아닙니다.");
        }

        return new MemoResponseDto(memo);
    }

    // 선택한 메모 수정 메서드
    @Transactional
    public MemoResponseDto updateMemo(String tokenValue, Long id, MemoRequestDto requestDto){
        // token에서 현재 user 조회
        User currentUser = getUserFromToken(tokenValue);

        // 해당 메모 존재하는지 확인
        Memo memo = findMemo(id);

        // 작성자만 수정할 수 있도록 확인
        if(currentUser != memo.getUser()){
            new IllegalArgumentException("해당 메모를 작성한 사용자가 아닙니다.");
        }

        memo.update(requestDto);

        return new MemoResponseDto(memo);
    }

    // 선택한 메모 삭제 메서드
    public ResponseEntity<String> deleteMemo(String tokenValue, Long id){
        // token에서 현재 user 조회
        User currentUser = getUserFromToken(tokenValue);

        // 해당 메모 존재하는지 확인
        Memo memo = findMemo(id);

        // 작성자만 삭제할 수 있도록 확인
        if(currentUser != memo.getUser()){
            new IllegalArgumentException("해당 메모를 작성한 사용자가 아닙니다.");
        }

        memoRepository.delete(memo);

        return ResponseEntity.ok("메모를 삭제했습니다.");
    }

    // 메모 찾는 메서드
    Memo findMemo(Long id){
        return memoRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("선택한 메모가 존재하지 않습니다."));
    }

    // token을 가공하고 해당 user 객체를 반환하는 메서드
    public User getUserFromToken(String tokenValue){
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);

        // username 가져오기 (username = loginId)
        String username = info.getSubject();

        // username으로 현재 사용자 조회하기
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return user;
    }
}