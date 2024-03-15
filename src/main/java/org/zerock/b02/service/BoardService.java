package org.zerock.b02.service;

import org.zerock.b02.domain.Board;
import org.zerock.b02.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

//    댓글 수까지 가져옴
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

//    게시글의 이미지와 댓글 수까지 같이 가져옴
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

//    DTO 객체를 Entity 객체로 변환하는 메서드
//    modelMapper 는 단순한 객체 매핑에는 편리하지만 다양한 처리가 필요한 경우 더 복잡함
//    DTO 와 entity 를 처리하는 경우가 많으므로 default 메서드로 작성함
//    근데 일단 modelMapper 로 매핑한 다음에 이미지는 따로 처리하면 되는 거 아닌가? 이 메서드도 지금 그렇게 하고 있는데? (24/03/15)
    default Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

//        이미지가 있는 글이면
        if(boardDTO.getFileNames() != null){

            boardDTO.getFileNames().forEach(fileNames -> {
//                "_"로 나누게 되면 "_"가 여러 개 포함된 이미지일 경우 확장자가 저장되지 못하는 현상 발생
//                마지막 _를 찾아서 substring(uuid), 그 뒤부터 마지막까지 substring(오리지널 파일 이름) 이렇게 바꿔야함(24/03/16)
                String[] arr = fileNames.split("_");
                System.out.println(Arrays.toString(arr));
                board.addImage(arr[0], arr[1]);
            });

        }// if

        return board;
    }


//    Entity 를 DTO 객체로 매핑하는 변환하는 메서드
//    modelMapper 는 단순한 객체 매핑에는 편리하지만 다양한 처리가 필요한 경우 더 복잡함
//    DTO 와 entity 를 처리하는 경우가 많으므로 default 메서드로 작성함
//    근데 일단 modelMapper 로 매핑한 다음에 이미지는 따로 처리하면 되는 거 아닌가? 이 메서드도 지금 그렇게 하고 있는데? (24/03/15)
    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board
                .getImageSet()
                .stream()
                .map(boardImage ->
                    boardImage.getUuid() + "_" + boardImage.getFileName())
                .toList();  //  collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;

    }

}
