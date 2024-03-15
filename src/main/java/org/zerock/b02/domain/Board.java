package org.zerock.b02.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "imageSet")
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

    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    @BatchSize(size = 20)   //  BatchSize 가 없으면 페이지 하나를 불러올 때 페이지 당 게시글 개수 만큼의 쿼리를 실행해서 문제가 됨. 이 N번의 쿼리를 한번에 실행할 수 있게 해준다.
    private Set<BoardImage> imageSet = new HashSet<>();

//    Board 객체 내에서 BoardImage를 모두 관리함.
    public void addImage(String uuid, String fileName){

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();

        imageSet.add(boardImage);
    }


//    BoardImage의 Board 참조를 null로 변경함
    public void clearImage(){
        imageSet.forEach(boardImage -> {
            boardImage.changeBoard(null);
        });

        this.imageSet.clear();
    }


}
