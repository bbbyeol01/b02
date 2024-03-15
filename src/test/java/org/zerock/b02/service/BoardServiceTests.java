package org.zerock.b02.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b02.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

//        첨부 파일 하나 추가
//        첨부 파일이 존재하는 게시글 수정은 첨부파일을 모두 지운 뒤 다른 이름으로 새로 등록하는 것
//        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));
        boardDTO.setFileNames(List.of(UUID.randomUUID() + "_zzz.jpg"));

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


    @Test
    public void registerWithImagesTest(){
        log.info("boardService " + boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File Sample title......")
                .content("File Sample Content......")
                .writer("user00")
                .build();

        boardDTO.setFileNames(Arrays.asList(
                UUID.randomUUID() + "_aaa.jpg",
                UUID.randomUUID() + "_bbb.jpg",
                UUID.randomUUID() + "_bbb.jpg"
        ));

        Long bno = boardService.register(boardDTO);
    }


    @Test
    public void readAllTest(){
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info("read..." + boardDTO);

        for(String fileName : boardDTO.getFileNames()){
            log.info("fileName " + fileName);
        }
    }

    @Test
    public void removeAllTest(){
        Long bno = 1L;

//        댓글이 없는 게시글의 삭제만 구현되어있음
//        게시글을 삭제하기 전에 이미지부터 삭제
        boardService.remove(bno);
    }

    @Test
    public void listWithAllTest(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> pageResponseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = pageResponseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO ->  {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()){
                    log.info("boardImage " + boardImageDTO);
                }// for
            }// if

            log.info("-----------------");
        });// for Each

    }
}
