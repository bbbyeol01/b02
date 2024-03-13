package org.zerock.b02.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

//    시작 페이지 번호
    private int start;

//    끝 페이지 번호
    private int end;

//    이전/다음 페이지 여부
    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total){

//        게시글 개수가 0이면 페이징 처리하지 않음
        if(total <= 0){
            return;
        }

//        현재 몇 페이지?
        this.page = pageRequestDTO.getPage();

//        페이지 당 글 개수?
        this.size = pageRequestDTO.getSize();

//        게시글 담긴 list
        this.dtoList = dtoList;
//        총 게시글 수
        this.total = total;

//        화면에서 마지막 번호
//        10으로 나누는 게 아니라 (double)size로 나눠야하는 거 아닌가...? 곱하는 것도 마찬가지
        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;

//        화면에서 시작 번호
//        여기가 9가 아니라 size - 1이어야 하는 거 아닌가?
        this.start = this.end - 9;

//        데이터 개수를 계산한 마지막 페이지 번호
        int last = (int)(Math.ceil((total / (double)size)));

        this.end = Math.min(end, last);

//        시작 번호가 1보다 크면 이전 페이지가 존재함
        this.prev = this.start > 1;

//        게시글 총 개수가 (마지막 번호 * 페이지 사이즈)보다 크면 다음 페이지가 존재함
        this.next = total > this.end * this.size;

    }
}
