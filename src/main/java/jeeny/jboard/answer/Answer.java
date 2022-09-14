package jeeny.jboard.answer;

import jeeny.jboard.question.Question;
import jeeny.jboard.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Getter
    @Setter
    @Entity
    public class Answer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(columnDefinition = "TEXT")
        private String content;

        private LocalDateTime createDate;

        @ManyToOne
        private Question question;
        //데이터를 가져오는 방식

        @ManyToOne
        private SiteUser author;
//        author 속성을 추가

    // 3-10  답변이 언제 수정되었는지 확인할 수 있도록 Answer 엔티티에 수정 일시를 의미하는 modifyDate 속성을 추가
        private LocalDateTime modifyDate;


        @ManyToMany
        Set<SiteUser> voter;

    }
