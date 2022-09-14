package jeeny.jboard.question;

import jeeny.jboard.answer.Answer;
import jeeny.jboard.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jeeny.jboard.DataNotFoundException;

import javax.persistence.criteria.*;

@RequiredArgsConstructor
@Service
//스프링의 서비스로 만들기 위해서는 위와 같이 클래스명 위에 @Service 애너테이션을 붙이면 된다.
public class QuestionService {

    private final QuestionRepository questionRepository;
//    questionRepository 객체는 생성자 방식으로 DI 규칙에 의해 주입된다.
//    public List<Question> getList() {
////        질문 목록을 조회하여 리턴하는 getList 메서드를 추가
//        return this.questionRepository.findAll();
//    }

//3-14검색추가
//추가한 search 메서드는 검색어(kw)를 입력받아 쿼리의 조인문과 where문을 생성하여 리턴하는 메서드이다.
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                q - Root, 즉 기준을 의미하는 Question 엔티티의 객체 (질문 제목과 내용을 검색하기 위해 필요)
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
//                u1 - Question 엔티티와 SiteUser 엔티티를 아우터 조인(JoinType.LEFT)하여 만든 SiteUser 엔티티의 객체.
//                Question 엔티티와 SiteUser 엔티티는 author 속성으로 연결되어 있기 때문에 q.join("author")와 같이 조인해야 한다. (질문 작성자를 검색하기 위해 필요)
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
//                a - Question 엔티티와 Answer 엔티티를 아우터 조인하여 만든 Answer 엔티티의 객체.
//                Question 엔티티와 Answer 엔티티는 answerList 속성으로 연결되어 있기 때문에 q.join("answerList")와 같이 조인해야 한다. (답변 내용을 검색하기 위해 필요)
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
//                u2 - 바로 위에서 작성한 a 객체와 다시 한번 SiteUser 엔티티와 아우터 조인하여 만든 SiteUser 엔티티의 객체 (답변 작성자를 검색하기 위해서 필요)
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
// 검색어(kw)가 포함되어 있는지를 like로 검색하기 위해 제목, 내용, 질문 작성자, 답변 내용, 답변 작성자 각각에 cb.like를 사용하고 최종적으로 cb.or로 OR 검색되게 하였다.
            }
        };
    }

//3-14기존내용 하단내용으로 수정
//    public Page<Question> getList(int page) {
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
////sorts객체에 Sort.Order 객체로 구성된 리스트에 Sort.Order 내림차순(DESC) 객체를 추가하고
////Sort.by(소트리스트)로 소트 객체를 생성할 수 있다.
//        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
////게시물을 역순으로 조회하기 위해서는 위와 같이 PageRequest.of 메서드의 세번째 파라미터로 Sort 객체를 전달해야 한다.
//        //스프링 데이터는 pageable에 PageRequest 객체를 주입해준다.
////        page: 현재 페이지, 0부터 시작한다.
////        size: 한 페이지에 노출할 데이터 건수
////        sort: 정렬 조건을 정의한다.
////        예) 정렬 속성,정렬 속성...(ASC | DESC), 정렬 방향을 변경하고 싶으면 sort 파라미터 추가 ( asc 생략 가능)
//        return this.questionRepository.findAll(pageable);
//    }

    //3-14_1 기존 하단내용 수정
    public Page<Question> getList(int page, String kw) {
        //검색어를 의미하는 매개변수 kw를 getList에 추가하고
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
//        kw 값으로 Specification 객체를 생성하여
        return this.questionRepository.findAll(spec, pageable);
//        findAll 메서드 호출시 전달하였다.
    }
    //3-14기존 하단내용 수정여기까지

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {//isPresent는 ! = null 과 같은
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    //    id 값으로 Question 데이터를 조회하는 getQuestion 메서드를 추가했다.
//    리포지터리로 얻은 Question 객체는 Optional 객체이기 때문에
//    isPresent 메서드로 해당 데이터가 존재하는지 검사하는 로직이 필요하다.
//Optional 객체를 사용하면 예상치 못한 NullPointerException 예외를 제공되는 메소드로 간단히 회피할 수 있다.
//즉, 복잡한 조건문 없이도 널(null) 값으로 인해 발생하는 예외를 처리할 수 있다.

//만약 id 값에  .isPresent() 해당하는 값이 있으면 반환하고
//Question 데이터가 없을 경우에는 DataNotFoundException을 발생시키도록 했다. DataNotFoundException 클래스는 아직 존재하지 않기 때문에 컴파일 오류가 발생할 것이다. DataNotFoundException 클래스를 작성

    public Question create(String subject, String content, SiteUser user) {
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(user);
        this.questionRepository.save(question);
        return question;
    }


//    제목과 내용을 입력으로 하여 질문 데이터를 저장하는 create 메서드를 만들었다.



//  3-10  질문 데이터를 수정할수 있는 modify 메서드를 추가
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

// 3-10_1   Question 객체를 입력으로 받아 Question 리포지터리를 사용하여 질문 데이터를 삭제하는 delete 메서드를 추가했다.
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }
//3-11 Question 엔티티에 사용자를 추천인으로 저장하는 vote 메서드를 추가했다.
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

}

