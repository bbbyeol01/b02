package org.zerock.b02.domain.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b02.domain.Board;
import org.zerock.b02.domain.QBoard;

import java.util.List;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board;

//        select ... from board
        JPQLQuery<Board> query = from(board);

//        where title like
        query.where(board.title.contains("1"));

//        페이징
        this.getQuerydsl().applyPagination(pageable, query);

//        쿼리 실행
        List<Board> list = query.fetch();

//        count 쿼리
        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;

//        select ... from board
        JPQLQuery<Board> query = from(board);

//        검색 조건과 키워드가 있다면
        if((types != null && types.length > 0) && keyword != null ){
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types){

                switch (type){
                    case "t" :
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c" :
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w" :
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }// for

            query.where(booleanBuilder);

        }// if

//        bno > 0
        query.where(board.bno.gt(0L));

//        페이징
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

//        페이징 처리의 최종 리턴 결과는 List<Board>가 아니라 Page<Board>여야 한다.
//        그래야 전/후페이지가 있는지, 총 페이지 수, 총 게시글 수를 알 수 있기 때문이다.
//        이를 위해 Spring Data JPA는 PageImpl이라는 클래스를 제공한다.
//        PageImpl(실제 목록 데이터, 페이지 관련 정보를 가진 객체, 전체 개수)
//        PageImpl(list, pageable, count)
        return new PageImpl<>(list, pageable, count);
    }


}

