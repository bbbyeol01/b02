package org.zerock.b02.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Member;
import org.zerock.b02.dto.MemberJoinDTO;
import org.zerock.b02.repository.MemberRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        
        String mid = memberJoinDTO.getMid();

//        findById (반환값: Optional 객체), existById (반환값: boolean)
        boolean exist = memberRepository.existsById(mid);

//        이미 가입되어 있으면 예외
        if(exist){
            throw new MidExistException();
        }

//        memberJoinDTO -> member
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(member.getMpw()));

        log.info("========================");
        log.info("member " + member);
        log.info("memberRoleSet " + member.getRoleSet());

        memberRepository.save(member);

    }


}
