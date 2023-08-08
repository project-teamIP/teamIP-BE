package com.teamip.heyhello.domain.memo.controller;

import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.memo.dto.MemoResponseDto;
import com.teamip.heyhello.domain.memo.entity.Memo;
import com.teamip.heyhello.domain.memo.service.MemoService;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

    // 사용자가 작성한 메모 5개씩 페이징 조회 API
    @GetMapping("/memos")
    public ResponseEntity<Page<Memo>> getMemos(@RequestHeader("Authorization") String tokenValue,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "5") int size) {
        // 페이지 번호가 1부터 시작
        Page<Memo> memoPage = memoService.findMemosWithPaging(tokenValue, page-1, size);
        return new ResponseEntity<>(memoPage, HttpStatus.OK);
    }

    // 메모 수정 API
    @PutMapping("/memo/{id}")
    public MemoResponseDto updateMemo(@RequestHeader("Authorization") String tokenValue,
                                      @PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(tokenValue, id, requestDto);
    }

    // 메모 삭제 API
    @DeleteMapping("/memo/{id}")
    public StatusResponseDto deleteMemo(@RequestHeader("Authorization") String tokenValue, @PathVariable Long id) {
        return memoService.deleteMemo(tokenValue, id);
    }
}