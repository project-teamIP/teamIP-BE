package com.teamip.heyhello.domain.memo.dto;

import com.teamip.heyhello.domain.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {

    private Long id;

    private String title;

    private String content;

    public MemoResponseDto(Memo memo){
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.content = memo.getContent();
    }
}
