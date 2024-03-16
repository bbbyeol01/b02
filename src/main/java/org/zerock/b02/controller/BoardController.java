package org.zerock.b02.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.*;
import org.zerock.b02.service.BoardService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;


    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){


//        PageResponseDTO pageResponseDTO = boardService.list(pageRequestDTO);

//        댓글 수까지 같이 불러오는 메서드로 변경
//        PageResponseDTO<BoardListReplyCountDTO> pageResponseDTO = boardService.listWithReplyCount(pageRequestDTO);

//        댓글 수와 이미지까지 같이 불러오는 메서드로 변경
        PageResponseDTO<BoardListAllDTO> pageResponseDTO = boardService.listWithAll(pageRequestDTO);

        log.info("응답 객체: " + pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

    }

    @GetMapping("/register")
    public void registerGet() {

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("board register(POST).............");

        if(bindingResult.hasErrors()){
            log.info("error!");

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info("boardDTO " + boardDTO);

        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";


    }

    @GetMapping({"/read", "/modify"})   //  read와 modify에 접속했을 때 해당 글을 model로 반환
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info("[read] " + boardDTO);

        model.addAttribute("boardDTO", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        log.info("board modify.........." + boardDTO);

        if(bindingResult.hasErrors()){
            log.info("error!");

            String link = pageRequestDTO.getLink();

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";

    }

    @PostMapping("/delete")
    public String delete(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        Long bno = boardDTO.getBno();
        log.info("remove post number........." + bno);

        boardService.remove(bno);

//        게시물이 DB 에서 삭제되었다면 첨부 파일도 DB 에서 삭제
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null){
            removeFiles(fileNames);
        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

    private void removeFiles(List<String> fileNames) {

        for(String fileName : fileNames){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename();

            try {

                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

//                썸네일이 존재한다면
                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }// if


            } catch (IOException e) {
                log.error(e.getMessage());
            }// try-catch


        }// for
    }



}
