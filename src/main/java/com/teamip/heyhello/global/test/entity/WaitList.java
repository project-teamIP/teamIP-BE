//package com.teamip.heyhello.global.test.entity;
//
//import com.teamip.heyhello.domain.match.dto.WaitUserDto;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.UUID;
//
//@Entity
//@Getter
//@NoArgsConstructor
//public class WaitList {
//    @Id
//    @Column(name = "wait_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column
//    private Long userId;
//    @Column
//    private String nickname;
//    @Column
//    private String language;
//    @Column
//    private UUID sessionId;
//
//    @Builder
//    public WaitList(Long userId, String nickname, String language, UUID sessionId) {
//        this.userId = userId;
//        this.nickname = nickname;
//        this.language = language;
//        this.sessionId = sessionId;
//    }
//    @Builder
//    public WaitList(WaitUserDto waitUserDto) {
//        this.userId = waitUserDto.getUserId();
//        this.nickname = waitUserDto.getNickname();
//        this.language = waitUserDto.getLanguage();
//        this.sessionId = waitUserDto.getSessionId();
//    }
//}
