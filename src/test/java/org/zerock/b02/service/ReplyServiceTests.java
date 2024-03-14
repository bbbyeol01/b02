package org.zerock.b02.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.domain.Reply;
import org.zerock.b02.dto.ReplyDTO;

@SpringBootTest
@Slf4j
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void replyRegisterTest(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("댓글...")
                .replyer("replyer")
                .bno(300L)
                .build();

        log.info("replyDTO.rno ....." + replyService.register(replyDTO));

    }
}
