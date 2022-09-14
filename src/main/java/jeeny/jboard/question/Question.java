package jeeny.jboard.question;


import jeeny.jboard.answer.Answer;
import jeeny.jboard.user.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    id는 Question 엔티티의 기본 키(Primary Key)이다.
//    id는 앞에서 엔티티를 생성할 때 설정했던대로 데이터를 생성할 때 속성값이 자동으로 1씩 증가하는 것을 확인할 수 있다.

//    Question 엔티티의 id는 @GeneratedValue 설정을 함

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)

//데이터를 가져오는 방식
    private List<Answer> answerList;

    //    author 속성은 SiteUser 엔티티를 @ManyToOne으로 적용했다.
    //    여러개의 질문이 한 명의 사용자에게 작성될 수 있으므로 @ManyToOne 관계가 성립한다.
    @ManyToOne
    private SiteUser author;

// 3-10   질문이나 답변이 언제 수정되었는지 확인할 수 있도록 Question 엔티티에 수정 일시를 의미하는 modifyDate 속성을 추가하자
    private LocalDateTime modifyDate;

    //3-11 추천인(voter) 속성을 추가
    //하나의 질문에 여러사람이 추천할 수 있고 한 사람이 여러 개의 질문을 추천할 수 있다.
    // 질문과 추천인은 부모와 자식의 관계가 아니고 대등한 관계이기 때문에 @ManyToMany를 사용해야 한다.
//    Set<SiteUser> voter 처럼 추천인(voter)을 @ManyToMany 관계로 추가했다. List가 아닌 Set으로 한 이유는 추천인은 중복되면 안되기 때문이다.
//    Set은 중복을 허용하지 않는 자료형이다.
    @ManyToMany
    Set<SiteUser> voter;


}
