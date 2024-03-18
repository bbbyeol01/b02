package org.zerock.b02.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b02.domain.Member;
import org.zerock.b02.domain.MemberRole;
import org.zerock.b02.repository.MemberRepository;
import org.zerock.b02.security.dto.MemberSecurityDTO;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor    //  생성자 주입
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public OAuth2User loadUser(OAuth2UserRequest userRequest){

        log.info("userRequest......" + userRequest);

        log.info("oauth2 user............");


        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("NAME " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName){
            case "kakao" :
                email = getKakaoEmail(paramMap);
                break;
        }

//        이메일로 가입여부 파악한 뒤 가입되지 않았으면 회원가입, 가입되어있으면 MemberSecurityDTO 반환
        return generateDTO(email, paramMap);

    }


    public MemberSecurityDTO generateDTO(String email, Map<String, Object> params){

        Optional<Member> result = memberRepository.findByEmail(email);

//        데이터베이스에 해당 이메일로 가입한 회원이 없다면
        if(result.isEmpty()){

//            회원 가입
            Member member = Member.builder()
                    .mid(email)
//                    1111로 비밀번호가 고정되긴 하지만 일반 로그인으로는 가입할 수 없음(findByEmail은 social을 가져오기 때문이다)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)   //  소셜 가입
                    .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

//            MemberSecurityDTO 로 반환
            return new MemberSecurityDTO(email, "1111", email,
                    false, true,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

        }


//        데이터베이스에 가입된 이메일이라면 memberSecurityDTO로 변환해서 반환
        Member member = result.get();
        return new MemberSecurityDTO(
                        member.getMid(),
                        member.getMpw(),
                        member.getEmail(),
                        member.isDel(),
                        member.isSocial(),
                        member.getRoleSet()
                                .stream()
                                .map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                .collect(Collectors.toList()));

    }




    public String getKakaoEmail(Map<String, Object> paramMap){

        log.info("------------------KAKAO------------------");

        Object value = paramMap.get("kakao_account");
        log.info("kakao account " + value);

        LinkedHashMap accountHashMap = (LinkedHashMap) value;
        String email = (String) accountHashMap.get("email");

        log.info("kakao Email " + email);

        return email;
    }

}
