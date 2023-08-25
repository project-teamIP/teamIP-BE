package com.teamip.heyhello.domain.email.controller;

import com.teamip.heyhello.domain.email.dto.EmailRequestDto;
import com.teamip.heyhello.global.swagger.SwaggerController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface EmailSwaggerController extends SwaggerController {

    @Operation(summary = "이메일 검증 메일 발송", description = "가입 시 이메일 유효성 검증 메일 발송 요청 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정상 응답", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = String.class)),})
    })
    public String sendEmail(@RequestBody EmailRequestDto emailRequestDto);
}
