package org.zerock.b02.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.dto.BoardDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;

import java.util.List;

@SpringBootTest
@Slf4j
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void registerTest(){

//        여러 번의 데이터베이스 연결이 있을 수 있으므로 트랜잭션 처리를 기본적으로 적용해야함
//        @Transactional 애노테이션을 적용했기 때문에 스프링이 해당 객체를 감싸는 별도의 클래스를 만듦
//        [boardService class name] org.zerock.b02.service.BoardServiceImpl$$SpringCGLIB$$0
        log.info("[boardService class name] " + boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample title...")
                .content("Sample content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("[after register bno] " + bno);
    }

    @Test
    public void readOneTest(){
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info("[readOne] " + boardDTO);
    }

    @Test
    public void modifyTest(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("modified Title...")
                .content("modified Content...")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void listTest(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")    //  제목, 내용, 작성자 모두 검색함
                .keyword("1")   //  1을 검색함
                .page(1)        //  1페이지
                .size(10)       //  페이지당 10개씩
                .build();


//        페이지 요청 객체로 페이지 응답 객체를 받아옴
        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.list(pageRequestDTO);

        log.info("[pageResponseDTO] " + pageResponseDTO);
        log.info("board list: " + pageResponseDTO.getDtoList());
        log.info("total page: " + pageResponseDTO.getTotal());
        log.info("current page: " + pageResponseDTO.getPage());
        log.info("start: " + pageResponseDTO.getStart());
        log.info("end: " + pageResponseDTO.getEnd());

    }
}
