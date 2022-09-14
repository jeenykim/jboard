package jeeny.jboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jeeny.jboard.answer.Answer;
import jeeny.jboard.answer.AnswerRepository;
import jeeny.jboard.question.Question;
import jeeny.jboard.question.QuestionRepository;
import jeeny.jboard.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class JboardApplicationTests {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionService questionService;

    //@Transactional
//    @Transactional 애너테이션을 사용하면 메서드가 종료될 때까지 DB 세션이 유지
//    @Test
//    void testJpa() {
//        Optional<Question> oq = this.questionRepository.findById(2);
//        assertTrue(oq.isPresent());
//        Question q = oq.get();
//
//        List<Answer> answerList = q.getAnswerList();
//        //질문 엔티티에 정의한 answerList를 사용
//
//        assertEquals(1, answerList.size());
//        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
//    }

    @Test
    void testJpa() {
        for (int i = 1; i <= 50; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
    }
//    @SpringBootTest 애너테이션은 SbbApplicationTests 클래스가 스프링부트 테스트 클래스임을 의미한다.
//@Autowired 애너테이션은 스프링의 DI 기능으로 questionRepository 객체를 스프링이 자동으로 생성해 준다.
//        DI(Dependency Injection) - 스프링이 객체(테이블내용삽입)를 대신 생성하여 주입한다.



