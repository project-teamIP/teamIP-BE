package com.teamip.heyhello.domain.memo.controller;

import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.memo.dto.MemoResponseDto;
import com.teamip.heyhello.domain.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    // 메모 작성 API
    @PostMapping("/memo")
    public MemoResponseDto createBlog(@RequestHeader("Authorization") String tokenValue,
                                      @RequestBody MemoRequestDto requestDto) {
        return memoService.createMemo(tokenValue, requestDto);
    }

    // 사용자가 작성한 메모 목록 조회 API
    @GetMapping("/memo")
    public List<MemoResponseDto> getMemoList(@RequestHeader("Authorization") String tokenValue) {
        return memoService.getMemoList(tokenValue);
    }

    // 사용자가 작성한 특정 메모 조회 API
    @GetMapping("/memo/{id}")
    public MemoResponseDto getMemoById(@RequestHeader("Authorization") String tokenValue,
                                       @PathVariable Long id){
        return memoService.getMemoById(tokenValue, id);
    }

    // 메모 수정 API
    @PutMapping("/memo/{id}")
    public MemoResponseDto updateMemo(@RequestHeader("Authorization") String tokenValue,
                                      @PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(tokenValue, id, requestDto);
    }

    // 메모 삭제 API
    @DeleteMapping("/memo/{id}")
    public ResponseEntity<String> deleteMemo(@RequestHeader("Authorization") String tokenValue, @PathVariable Long id) {
        return memoService.deleteMemo(tokenValue, id);
    }
}