package com.teamip.heyhello.domain.report.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportCategory {
    ABUSIVE_LANGUAGE("언어 폭력 및 욕설"),
    SEXUAL_HARASSMENT("성적 모독"),
    SCAM("광고, 홍보, 사기"),
    COPYRIGHT_VIOLATION("광고, 홍보, 사기"),
    DISCRIMINATION("편견, 차별/혐오 발언");

    private final String displayName;
}
