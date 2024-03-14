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
import org.zerock.b02.dto.BoardListReplyCountDTO;

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
        Long bno = 300L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info("select reseult............ {}", board);


    }

    @Test
    public void updateTest(){
        Long bno = 300L;

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
    public void pagingTest(){
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

    @Test
    public void search1Test(){

//        1부터 10까지 bno로 내림차순 정렬
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        Page<Board> boardList = boardRepository.search1(pageable);

        log.info("boardList........ {}", boardList);

    }

    @Test
    public void searchAllTest(){

        Pageable pageable = PageRequest.of(2, 10, Sort.by("bno").descending());

        String[] types = {"t", "c", "w"};

        Page<Board> boardList = boardRepository.searchAll(types, "title", pageable);

    }

    @Test
    public void searchAllTest2(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        String[] types = {"t", "c", "w"};

        Page<Board> boardList = boardRepository.searchAll(types, "title", pageable);

        log.info("total page: " + boardList.getTotalPages());
        log.info("page size: " + boardList.getSize());
        log.info("current page: " + boardList.getNumber());
        log.info("prev? " + boardList.hasPrevious() + " | next? " + boardList.hasNext());

        boardList.getContent().forEach( board -> {
            log.info("board: " + board);
        });
    }

    @Test
    public void searchReplyCountTest(){
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());


        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        result.getContent().forEach(board -> log.info("boardDTO" + board));
    }


}
