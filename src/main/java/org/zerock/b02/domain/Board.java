package org.zerock.b02.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  Auto Increment
    private Long bno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

//    엔티티 객체는 불변하도록 설계하는 것이 좋으나, 강제적인 사항은 아니므로 수정이 필요하다면 수정이 가능한 부분만 메서드로 설계한다
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }


}
