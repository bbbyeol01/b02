package org.zerock.b02.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Member;
import org.zerock.b02.repository.MemberRepository;
import org.zerock.b02.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // 생성자 주입
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Username Not Found......");
        }

        Member member = result.get();

//        member -> memberSecurityDTO
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(), member.getMpw(), member.getEmail(),
                member.isDel(), false,
                member.getRoleSet().stream()
                        .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                        .collect(Collectors.toList())
        );

        log.info("memberSecurityDTO " + memberSecurityDTO);


        return memberSecurityDTO;





    }
}
