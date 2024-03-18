package org.zerock.b02.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    private String mid;

    private String mpw;
    private String email;

//    soft delete -> del 처리 대신 삭제 테이블을 따로 만들어두는 것 고려
    private boolean del;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();



//    mpw, email, del setter
    public void changePassword(String mpw){
        this.mpw = mpw;
    }

    public void changeEmail(String email){
        this.email = email;
    }

    public void changeDel(boolean del){
        this.del = del;
    }

//    권한 추가
    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

//    권한 삭제
    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean social){
        this.social = social;
    }

}
