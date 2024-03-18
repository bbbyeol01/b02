package org.zerock.b02.service;

import org.zerock.b02.dto.MemberJoinDTO;

public interface MemberService {

//    아이디가 이미 존재할 때 던지는 예외
    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
