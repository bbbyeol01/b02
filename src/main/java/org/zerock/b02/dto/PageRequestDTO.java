package org.zerock.b02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

//    검색 카테고리: t(title), c(content), w(writer), tc, tw, twc
    private String type;

    private String keyword;

    private String link;

    public String[] getType(){
        if(type == null || type.isEmpty()){
            return null;
        }

        return type.split("");
    }

    public Pageable getPageable(String... props){

//        PageRequest.of(페이지 번호, 사이즈, Sort)
        return PageRequest.of(this.page - 1, this.size, Sort.by("bno").descending());
    }

    public String getLink(){
        if(link == null){
            StringBuilder sb = new StringBuilder();

            sb.append("page=").append(this.page).append("&size=").append(this.size);

//            type이 비어있으면
            if(type != null && !type.isEmpty()){
                sb.append("&type=").append(type);
            }

//            keyword가 null이면
            if(keyword != null){
                try {
                    sb.append("&keyword").append(URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }

//            ex) page=1&type=tcw&keyword=안녕하세요
            link = sb.toString();
        }

        return link;
    }


}
