package org.zerock.b02.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Board;
import org.zerock.b02.dto.*;
import org.zerock.b02.repository.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {

//        modelMapper 로 BoardDTO -> Board 매핑
//        Board board = modelMapper.map(boardDTO, Board.class);

        Board board = dtoToEntity(boardDTO);

        return boardRepository.save(board).getBno();

    }

    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Board> result = boardRepository.findByIdWithImages(bno);

//        데이터베이스와 완전히 일치하는 엔티티 객체
        Board board = result.orElseThrow();

//        엔티티 -> dto 변환
        return entityToDTO(board);
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.update(boardDTO.getTitle(), boardDTO.getContent());

//        첨부 파일 처리
        board.clearImage();

        if(boardDTO.getFileNames() != null){
            for(String fileName : boardDTO.getFileNames()){
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }// for
        }// if

        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();

//        List<BoardDTO>로 변환되어야 함
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

//        변환 과정
//        modelMapper를 사용하여 변환할 수 있음
//        Page<Board>에 담긴 내용을 하나씩 까서 board -> BoardDTO로 매핑함(변환)
//        변환된 스트림을 List로 바꿈
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());

//        PageResponseDTO build
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

//        withAll은 Build와 같지만 필드를 모두 사용하겠다는 뜻
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable();

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();

    }
}
