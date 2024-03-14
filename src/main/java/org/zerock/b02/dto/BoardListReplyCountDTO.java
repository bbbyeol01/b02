package org.zerock.b02.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
// 댓글이 추가되었기 때문에 목록에 댓글 수를 출력할 DTO를 받아와야함
public class BoardListReplyCountDTO {

    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;

    private Long replyCount;
}
