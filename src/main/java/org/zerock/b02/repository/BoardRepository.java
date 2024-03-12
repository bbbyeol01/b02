package org.zerock.b02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b02.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
