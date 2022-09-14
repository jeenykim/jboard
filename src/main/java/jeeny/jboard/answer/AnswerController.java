package jeeny.jboard.answer;


import jeeny.jboard.question.Question;
import jeeny.jboard.question.QuestionService;
import jeeny.jboard.user.SiteUser;
import jeeny.jboard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    private final UserService userService;


    //처음
//    @PostMapping("/create/{id}")
//    public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam String content) {
//        Question question = this.questionService.getQuestion(id);
//        // 답변을 저장해야함
//        this.answerService.create(question, content);
//        return String.format("redirect:/question/detail/%s", id);
////        %s   문자형 출력
//    }

//   답변 컨트롤러의 URL 프리픽스(주소)도 /answer로 고정했다.
//    /answer/create/{id}와 같은 URL 요청시 createAnswer 메서드가 호출되도록 @PostMapping으로 매핑했다.
//    @PostMapping은 @RequestMapping과 동일하게 매핑을 담당하는 역할을 하지만 POST요청만 받아들일 경우에 사용한다. 만약 위 URL을 GET방식으로 요청할 경우에는 오류가 발생한다.
//
//@PostMapping(value="/create/{id}") 대신 @PostMapping("/create/{id}") 처럼 value는 생략해도 된다.
//
// createAnswer 메서드의 매개변수에는 @RequestParam String content 항목이 추가되었다.
// 이 항목은 템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가되었다.
// 템플릿의 답변 내용에 해당하는 textarea의 name 속성명이 content이기 때문에 여기서도 변수명을 content로 사용해야 한다. 만약 content 대신 다른 이름으로 사용하면 오류가 발생할 것이다.
//
//createAnswer 메서드의 URL 매핑 /create/{id}에서 {id}는 질문의 id 이므로 이 id 값으로 질문을 조회하고 없을 경우에는 404 오류가 발생할 것이다.
// 하지만 아직 답변을 저장하는 코드를 작성하지 않고 일단 다음과 같은 주석으로 답변을 저장해야 함을 나타내었다.

//%d	정수형 출력
// %s   문자형 출력
// %f	실수형 출력
//%c	문자열 출력
//%n	줄 바꿈
// %b	boolean 출력

// 답변을 저장해야함 위치에 답변저장추가
//    주석문을 위치에 AnswerService의 create 메서드를 호출하여 답변을 저장할수 있게 했다.
//
//이제 다시 질문 상세 페이지에 접속하여 텍스트 창에 아무 값이나 입력하고 <답변등록>을 눌러 보자. 답변은 잘 저장되었다.
//템플릿에 표시하는 기능필요


//3-8수정
@PreAuthorize("isAuthenticated()")
//    2-15 수정
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
//        AnswerForm을 사용하도록 컨트롤러 변경
//        QuestionForm을 사용했던 방법과 마찬가지로 @Valid와 BindingResult를 사용하여 검증을 진행한다.

        //3-8 답변에 작성자 저장
//현재 로그인한 사용자에 대한 정보를 알기 위해서는 스프링 시큐리티가 제공하는 Principal 객체를 사용해야 한다.
// 위와 같이 createAnswer 메서드에 Principal 객체를 매개변수로 지정하면 된다.
//principal.getName()을 호출하면 현재 로그인한 사용자의 사용자명(사용자ID)을 알수 있다.
//principal 객체를 사용하면 이제 로그인한 사용자의 사용자명을 알수 있으므로 사용자명을 통해 SiteUser객체를 조회할 수 있다.
            Question question = this.questionService.getQuestion(id);
            SiteUser siteUser = this.userService.getUser(principal.getName());
            if (bindingResult.hasErrors()) {
                model.addAttribute("question", question);
                return "question_detail";
            }
//        검증에 실패할 경우에는 다시 답변을 등록할 수 있는 question_detail 템플릿을 렌더링하게 했다.
//        이때 question_detail 템플릿은 Question 객체가 필요하므로 model 객체에 Question 객체를 저장한 후에 question_detail 템플릿을 렌더링해야 한다.

//    3-12수정 기존삭제
//        this.answerService.create(question, answerForm.getContent(), siteUser);
//            return String.format("redirect:/question/detail/%s", id);
//        %s   문자형 출력

//    3-12앵커수정 기존에서 수정
    Answer answer = this.answerService.create(question,
            answerForm.getContent(), siteUser);
    return String.format("redirect:/question/detail/%s#answer_%s",
            answer.getQuestion().getId(), answer.getId());
        }

    //3-10_1추가
//answerModify 메서드를 추가했다.
// URL의 답변 아이디를 통해 조회한 답변 데이터의 "내용"을
// AnswerForm 객체에 대입하여 answer_form.html 템플릿에서 사용할수 있도록 했다.
// answer_form.html은 답변을 수정하기 위한 템플릿으로 신규로 작성해야 한다.
//답변 수정시 기존의 내용이 필요하므로 AnswerForm 객체에 조회한 값을 저장해야 한다.
@PreAuthorize("isAuthenticated()")
@GetMapping("/modify/{id}")
public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
    Answer answer = this.answerService.getAnswer(id);
    if (!answer.getAuthor().getUsername().equals(principal.getName())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }
    answerForm.setContent(answer.getContent());
    return "answer_form";
}

    //3-10_2추가
//    POST 방식의 답변 수정을 처리하는 answerModify 메서드를 추가했다.
//    답변 수정을 완료한 후에는 질문 상세 페이지로 돌아가기 위해
//    answer.getQuestion.getId()로 질문의 아이디를 가져왔다.
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.answerService.modify(answer, answerForm.getContent());

//        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        //        3-12위기존코드에서 앵커코드수정
//        리다이렉트되는 질문 상세 URL에 #answer_2와 같은 앵커를 추가한 것이다
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
    }


//        3-10_3추가
//답변을 삭제하는 answerDelete 메서드를 추가했다.
// 답변을 삭제한 후에는 해당 답변이 있던 질문상세 화면으로 리다이렉트 한다.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.answerService.delete(answer);
 return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());

    }

    //        3-11추가
//  answerVote 메서드를 추가했다.
//  추천은 로그인한 사람만 가능해야 하므로 @PreAuthorize("isAuthenticated()") 애너테이션이 적용되었다.
//    AnswerService의 vote 메서드를 호출하여 추천인을 저장한다. 오류가 없다면 질문 상세화면으로 리다이렉트 한다.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = this.answerService.getAnswer(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.vote(answer, siteUser);
        //        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        //        3-12위기존코드에서 앵커코드수정
        return String.format("redirect:/question/detail/%s#answer_%s",
                answer.getQuestion().getId(), answer.getId());
    }

}

