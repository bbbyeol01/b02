package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b02.domain.Board;
import org.zerock.b02.domain.search.BoardSearch;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

//    @Query(value = "select now()", nativeQuery = true)
//    String getTime();

    @EntityGraph(attributePaths = {"imageSet"}) // 같이 로딩해야하는 속성
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);
}
