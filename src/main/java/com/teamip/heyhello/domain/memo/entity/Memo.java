package com.teamip.heyhello.domain.memo.entity;

import com.teamip.heyhello.domain.memo.dto.MemoRequestDto;
import com.teamip.heyhello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 Entitu 클래스 저장
@Getter
@Table(name = "memo")
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    public Memo(User user, MemoRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.user = user;
        this.content = requestDto.getContent();
    }

    public void update(MemoRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
