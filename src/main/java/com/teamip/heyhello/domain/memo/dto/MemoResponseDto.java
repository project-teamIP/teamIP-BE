package com.teamip.heyhello.domain.memo.dto;

import com.teamip.heyhello.domain.memo.entity.Memo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemoResponseDto {

    private Long id;

    private String title;

    private String content;

    private String partnerNickname;

    private String partnerImage;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public MemoResponseDto(Memo memo){
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.content = memo.getContent();
        this.partnerNickname = memo.getPartnerNickname();
        this.partnerImage = memo.getPartnerImage();
        this.createdAt = memo.getCreatedAt();
        this.modifiedAt = memo.getModifiedAt();
    }
}
