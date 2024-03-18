package org.zerock.b02.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b02.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {   //  JpaRepository<테이블, 아이디 타입>

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);


    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    @Modifying  //  Query는 주로 select 할 때 사용하지만 @Modifying 과 함께 사용하면 DML(insert/update/delete) 처리도 가능함
    @Transactional
    @Query("update Member m set m.mpw = :mpw where m.mid = :mid")
    void updatePassword(@Param("mpw")String password, @Param("mid")String mid);
}
