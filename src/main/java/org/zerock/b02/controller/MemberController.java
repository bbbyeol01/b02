package org.zerock.b02.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b02.dto.MemberJoinDTO;
import org.zerock.b02.service.MemberService;

@Controller
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor    //  생성자로 의존성 주입
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGET(String error, String logout){
        log.info("login get...............");
        log.info("logout: " + logout);

        if(logout != null){
            log.info("user logout.....................");
        }
    }

    @GetMapping("/join")
    public void joinGET(){
        log.info("join GET......");
    }

    @PostMapping("/join")
    public String joinPost(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post......");
        log.info("memberJoinDTO " + memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/board/list";

    }

}
