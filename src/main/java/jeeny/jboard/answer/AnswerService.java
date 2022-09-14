package jeeny.jboard.answer;

import jeeny.jboard.DataNotFoundException;
import jeeny.jboard.question.Question;
import jeeny.jboard.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    //AnswerService에는 답변 생성을 위해 create 메서드를 추가했다.
// create 메서드는 입력으로 받은 question과 content를 사용하여 Answer 객체를 생성하여 저장했다.
//    public void create(Question question, String content, SiteUser author) {
//        Answer answer = new Answer();
//        answer.setContent(content);
//        answer.setCreateDate(LocalDateTime.now());
//        answer.setQuestion(question);
//        answer.setAuthor(author);
//        this.answerRepository.save(answer);
//    }

//상단 코드에서  3-12앵커수정
//컨트롤러에서 답변이 등록된 위치로 이동하기 위해서는 답변 객체가 반드시 필요하다.
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }


    //3-10추가
//답변 입력한 아이디로 답변을 조회하는 getAnswer 메서드를 추가했다.
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    //3-10추가
//현시점 답변의 내용으로 답변을 수정하는 modify 메서드를 추가했다.
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    // 3-10_1추가   답변을 삭제하는 기능을 추가
//    입력으로 받은 Answer 객체를 사용하여 답변을 삭제하는 delete 메서드를 추가했다.
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

//3-11 추가 Answer 엔티티에 사용자를 추천인으로 저장하는 vote 메서드를 추가했다.
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

}


