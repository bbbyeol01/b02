package org.zerock.b02.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.BoardDTO;
import org.zerock.b02.dto.BoardListReplyCountDTO;
import org.zerock.b02.dto.PageRequestDTO;
import org.zerock.b02.dto.PageResponseDTO;
import org.zerock.b02.service.BoardService;

@Controller
@RequestMapping("/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){


//        PageResponseDTO pageResponseDTO = boardService.list(pageRequestDTO);
//        댓글 수까지 같이 불러오는 메서드로 변경
        PageResponseDTO<BoardListReplyCountDTO> pageResponseDTO = boardService.listWithReplyCount(pageRequestDTO);

        log.info("응답 객체: " + pageResponseDTO);

        model.addAttribute("pageResponseDTO", pageResponseDTO);

    }

    @PreAuthorize("isAuthenticated()")  //  로그인한 유저만 글 작성할 수 있음
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

    @PreAuthorize("principal.username == #boardDTO.writer") //  로그인한 유저와 댓글 작성 유저가 같아야 modify 진입 가능
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

    @PreAuthorize("principal.username == #boardDTO.writer") //  로그인한 유저와 댓글 작성 유저가 같아야 modify 진입 가능
    @GetMapping("/delete")
    public String delete(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        log.info("remove post number........." + boardDTO.getBno());

        boardService.remove(boardDTO.getBno());

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

}
