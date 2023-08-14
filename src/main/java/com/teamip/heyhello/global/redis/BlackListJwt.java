package com.teamip.heyhello.global.redis;

import lombok.Getter;

@Getter
public class BlackListJwt {
    private String loginId;
    private String atk;

    public BlackListJwt(String loginId, String atk) {
        this.loginId = loginId;
        this.atk = atk;
    }
}
