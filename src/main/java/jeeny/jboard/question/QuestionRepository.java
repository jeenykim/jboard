package jeeny.jboard.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
//    제목으로 테이블 데이터를 조회
    //Question 리포지터리는 findBySubject와 같은 메서드를 기본적으로 제공하지는 않는다. findBySubject 메서드를 사용하려면 다음처럼 QuestionRepository 인터페이스를 변경해야 한다.

    Question findBySubjectAndContent(String subject, String content);
//    제목과 내용을 함께 조회 두 개의 속성을 And 조건으로 조회

    List<Question> findBySubjectLike(String subject);
//    제목에 특정 문자열이 포함되어 있는 데이터 조회
//응답 결과가 여러건인 경우에는 리포지터리 메서드의 리턴 타입을 Question이 아닌 List<Question> 으로 해야 한다.

// 3-02추가
    Page<Question> findAll(Pageable pageable);

//3-14추가
//QuestionService 맨 위에서 작성한 Specification과 Pageable 객체를 입력으로 Question 엔티티를 조회하는 findAll 메서드를 선언했다.
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}

//////////////////////기본//////////////////////////////////
//QuestionRepository는 리포지터리로 만들기 위해 JpaRepository 인터페이스를 상속했다. JpaRepository를 상속할 때는 제네릭스 타입으로 <Question, Integer> 처럼 리포지터리의 대상이 되는 엔티티의 타입(Question)과 해당 엔티티의 PK의 속성 타입(Integer)을 지정해야 한다. 이것은 JpaRepository를 생성하기 위한 규칙이다.
//Question 엔티티의 PK(Primary Key) 속성인 id의 타입은 Integer 이다.
//QuestionRepository, AnswerRepository를 이용하여 question, answer 테이블에 데이터를 저장하거나 조회할 수 있다.
//스프링문법규칙 findBy + 엔티티의 속성명(예:findBySubject)과 같은 리포지터리 메서드를 작성하면 해당 속성의 값으로 데이터를 조회할수 있다.