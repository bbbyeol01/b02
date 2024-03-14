package org.zerock.b02.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b02.domain.Reply;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.dto.ReplyDTO;
import org.zerock.b02.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Slf4j
//  의존성 주입을 위한 생성자
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "reply POST", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
//    replyDTO를 수집할 때 @Valid 적용, BindingResult를 매개변수로 추가
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {

        log.info("replyDTO........" + replyDTO);

//        에러가 발생하면 BindException throw
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }


//    replies/list/123(특정 게시글 번호)
    @Operation(summary = "replies of board", description = "GET 방식으로 특정 게시물의 댓글 목록을 불러옴")
    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){

        return replyService.getListOfBoard(bno, pageRequestDTO);
    }

//    replies/123(특정 댓글 번호)
    @Operation(summary = "read reply", description = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

        return replyService.read(rno);
    }

//    replies/123(특정 댓글 번호)
    @Operation(summary = "delete reply", description = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno){

//        없는 번호로 삭제 시에도 Exception 이 발생하지 않아 read 처리를 하였음 : 400
//        replyService.read(rno);

//        Service 단의 로직을 deleteById(rno) 에서 findById(rno) -> delete(rno)로 수정
        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }



    @Operation(summary = "modify reply", description = "PUT 방식으로 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){

        replyDTO.setRno(rno);

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();

        resultMap.put("rno", rno);

        return resultMap;
    }




}
