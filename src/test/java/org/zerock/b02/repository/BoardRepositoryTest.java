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

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //  테스트할 때 스프링 같이 올려줌
@Slf4j
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

//    insert 기능 테스트
    @Test
    public void insertTest(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content... " + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);

            log.info("BNO: {}", result.getBno());


        });
    }

    @Test
    public void selectTest(){
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info("select reseult............ {}", board);


    }

    @Test
    public void updateTest(){
        Long bno = 100L;

//        findById로 수정할 board를 불러와서
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

//        변경한 후 저장한다.
        board.update("update title!!!", "update content!!!");

        boardRepository.save(board);

    }

    @Test
    public void deleteTest(){
        Long bno = 1L;

        boardRepository.deleteById(bno);

    }

    @Test
    public void testPaging(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: " + result.getTotalElements());
        log.info("total pages: " + result.getTotalPages());
        log.info("page number: " + result.getNumber());
        log.info("page size: " + result.getSize());

        List<Board> contentList = result.getContent();
        contentList.forEach(board -> {
            log.info("board: " + board);
        });

    }


}
