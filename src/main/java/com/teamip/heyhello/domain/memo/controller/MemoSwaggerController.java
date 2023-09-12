package com.teamip.heyhello.domain.memo.controller;

import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.memo.dto.MemoResponseDto;
import com.teamip.heyhello.domain.memo.entity.Memo;
import com.teamip.heyhello.global.dto.StatusResponseDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MemoSwaggerController extends SwaggerController {
    @Operation(summary = "메모 작성", description = "대화 종료 시 메모 저장 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemoResponseDto.class))})
    })
    public MemoResponseDto createBlog(@RequestHeader("AccessToken") String tokenValue,
                                      @RequestBody MemoRequestDto requestDto);

    // 사용자가 작성한 메모 목록 조회 API
    @Operation(summary = "메모 조회", description = "본인 메모 전체 조회 api.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(type = "array", implementation = MemoResponseDto.class)))})
    })
    public List<MemoResponseDto> getMemoList(@RequestHeader("AccessToken") String tokenValue);

    // 사용자가 작성한 특정 메모 조회 API
    @Operation(summary = "메모 조회", description = "특정 메모 조회 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemoResponseDto.class)),})
    })
    public MemoResponseDto getMemoById(@RequestHeader("AccessToken") String tokenValue,
                                       @PathVariable Long id);

    // 사용자가 작성한 메모 6개씩 페이징 조회 API
    @Operation(summary = "메모 페이징 조회", description = "본인 메모 페이징 형식 조회 api.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(type = "array", implementation = MemoResponseDto.class)))})
    })
    public ResponseEntity<Page<Memo>> getMemos(@RequestHeader("AccessToken") String tokenValue,
                                               @RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "6") int size);

    // 메모 수정 API
    @Operation(summary = "메모 수정", description = "메모 수정 조회 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = MemoResponseDto.class))})
    })
    public MemoResponseDto updateMemo(@RequestHeader("AccessToken") String tokenValue,
                                      @PathVariable Long id, @RequestBody MemoRequestDto requestDto);

    // 메모 삭제 API
    @Operation(summary = "메모 삭제", description = "본인 메모 삭제 api.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = StatusResponseDto.class))})
    })
    public StatusResponseDto deleteMemo(@RequestHeader("AccessToken") String tokenValue, @PathVariable Long id);
}
