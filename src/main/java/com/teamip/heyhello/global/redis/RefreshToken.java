package com.teamip.heyhello.global.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class RefreshToken {
    private String loginId;
    private String rtk;
    private String exp;

    public RefreshToken(String loginId, String rtk, String exp) {
        this.loginId = loginId;
        this.rtk = rtk;
        this.exp = exp;
    }
}