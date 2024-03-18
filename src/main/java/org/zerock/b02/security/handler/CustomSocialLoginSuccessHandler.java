package org.zerock.b02.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.b02.security.dto.MemberSecurityDTO;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("--------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess....................");
        log.info("authentication principal " + authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodePw = memberSecurityDTO.getMpw();

//        소셜 로그인 회원이고, 패스워드가 1111이면
        if(memberSecurityDTO.isSocial()
                && (memberSecurityDTO.getMpw().equals("1111"))
                || passwordEncoder.matches("1111", memberSecurityDTO.getMpw())){

            log.info("비밀번호 변경이 필요합니다.");
            log.info("Redirect to Member Modify");
            response.sendRedirect("/member/modify");

            return;
        }

        response.sendRedirect("/board/list");

    }
}
