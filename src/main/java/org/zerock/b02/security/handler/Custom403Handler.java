package org.zerock.b02.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("---------------------ACCESS DINIED-----------------------");

        response.setStatus(HttpStatus.FORBIDDEN.value());

//        JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("is JSON " + jsonRequest);

        if(!jsonRequest){
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }




}
