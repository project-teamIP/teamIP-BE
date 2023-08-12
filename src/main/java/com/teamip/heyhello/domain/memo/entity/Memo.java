package com.teamip.heyhello.domain.memo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.user.entity.User;
import com.teamip.heyhello.global.audit.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 Entitu 클래스 저장
@Getter
@Table(name = "memo")
@NoArgsConstructor
public class Memo extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "partnerNickname", nullable = false)
    private String partnerNickname;

    @Column(name = "partnerImage", nullable = false)
    private String partnerImage;

    public Memo(User user, MemoRequestDto requestDto, String partnerImage){
        this.title = requestDto.getTitle();
        this.user = user;
        this.content = requestDto.getContent();
        this.partnerNickname = requestDto.getPartnerNickname();
        this.partnerImage = partnerImage;
    }

    public void update(MemoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
