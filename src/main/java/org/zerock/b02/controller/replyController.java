package org.zerock.b02.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b02.dto.ReplyDTO;

import java.util.Map;

@Controller
@RequestMapping("/replies")
@Slf4j
public class replyController {

    @Operation(summary = "reply POST", description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO){

        log.info("replyDTO........" + replyDTO);

        Map<String, Long> resultMap = Map.of("rno", 111L);

        return ResponseEntity.ok(resultMap);
    }



}
