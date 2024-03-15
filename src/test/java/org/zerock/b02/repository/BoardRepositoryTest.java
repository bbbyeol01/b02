package org.zerock.b02.repository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.b02.domain.Board;
import org.zerock.b02.dto.BoardListAllDTO;
import org.zerock.b02.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest //  테스트할 때 스프링 같이 올려줌
@Slf4j
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

//    insert 기능 테스트
    @Test
    public void insertTest(){
//        글 400개 만듦
        IntStream.rangeClosed(1, 400).forEach(i -> {
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

    @Test
    public void insertImageTest(){

        Board board = Board.builder()
                .title("Image test")
                .content("첨부 파일 테스트")
                .writer("tester")
                .build();

        for(int i = 0; i < 3; i++){
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }// for

        boardRepository.save(board);
    }

    @Test
    public void readWithImagesTest(){

        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info("board " + board);
        log.info("----------------");
        log.info("board image " + board.getImageSet());

//        테스트 코드에서는 데이터베이스와 연결이 끝나면 쿼리를 다시 날릴 수 없음(한번 날리면 끝)
//        @Transactional 을 적용하면 필요할 때마다 메서드 내에서 추가 쿼리를 여러번 실행할 수 있음

    }

    @Test
    public void modifyImagesTest(){
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

//        기존 첨부파일 삭제
        board.clearImage();

        for(int i = 0; i < 2; i++){
            board.addImage(UUID.randomUUID().toString(), "uploadFile" + i + ".jpg");
        }

        boardRepository.save(board);
    }


    @Test
    @Transactional
    @Commit
    public void removalAllTest(){

        Long bno = 1L;

//        댓글 먼저 삭제하고(하위 엔티티) 게시글 삭제
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);

    }

    @Test
    public void insertAllTest(){

        for(int i = 1; i <= 100; i++){
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("writer" + i)
                    .build();

            for(int j = 0; j < 3; j++){

//                글번호가 5의 배수면 첨부파일이 없는 글
                if(i % 5 == 0){
                    continue;
                }

                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }// inner for

            boardRepository.save(board);
        }// outer for

    }


    @Test
    @Transactional
    public void searchImageReplyCountTest(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        boardRepository.searchWithAll(null, null, pageable);

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("---------------------");
        log.info("total element " + result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> {
            log.info("boardListDTO " + boardListAllDTO);
        });
    }






}
