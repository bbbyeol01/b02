package org.zerock.b02.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b02.domain.Board;
import org.zerock.b02.domain.Reply;
import org.zerock.b02.dto.BoardListReplyCountDTO;

@SpringBootTest
@Slf4j
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertTest(){

        Long bno = 305L;

//        Board 객체는 Id 값을 가지도록 구성하는 것이 중요함
        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글...")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);

    }

    @Test
    public void boardRepliesTest(){

        Long bno = 305L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info("reply......" + reply);
        });
    }


}
