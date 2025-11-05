package com.example.suki.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "피드백 및 문의 요청")
public record SupportRequest(
        @Schema(description = "문의 유형", example = "오류 제보")
        @NotBlank(message = "문의 유형은 필수입니다.")
        String type,

        @Schema(description = "문의 내용", example = "특정 조건에서 시뮬레이션 결과가 나오지 않습니다.")
        @NotBlank(message = "내용은 필수입니다.")
        @Size(min = 10, max = 2000, message = "내용은 10자 이상 2000자 이하로 입력해주세요.")
        String message
) {
}
