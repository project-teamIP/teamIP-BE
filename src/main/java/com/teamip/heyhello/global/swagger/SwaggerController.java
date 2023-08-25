package com.teamip.heyhello.global.swagger;

import com.teamip.heyhello.global.dto.StatusResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400" ,description = "Bad Request Error", content =
        { @Content(mediaType = "application/json", schema =
        @Schema(implementation = StatusResponseDto.class)) }),
@ApiResponse(responseCode = "401" ,description = "Authorization Error", content =
        { @Content(mediaType = "application/json", schema =
        @Schema(implementation = StatusResponseDto.class)) }),
@ApiResponse(responseCode = "403", description = "Access Denied Error", content =
        { @Content(mediaType = "application/json", schema =
        @Schema(implementation = StatusResponseDto.class)) })
})

public interface SwaggerController {
}
