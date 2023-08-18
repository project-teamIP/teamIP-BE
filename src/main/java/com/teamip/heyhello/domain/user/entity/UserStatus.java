package com.teamip.heyhello.domain.user.entity;

public enum UserStatus {
    ACTIVE("활동중"),
    DORMANT("휴면"),
    SUSPENSION("정지"),
    WITHDRAWAL("탈퇴");

    private final String value;

    UserStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}