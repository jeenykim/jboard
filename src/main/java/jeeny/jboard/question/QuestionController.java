package jeeny.jboard.question;

import jeeny.jboard.answer.AnswerForm;
import jeeny.jboard.user.SiteUser;
import jeeny.jboard.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
//@RequiredArgsConstructor 애너테이션으로 questionRepository 속성을 포함하는 생성자를 생성하였다.
//@RequiredArgsConstructor는 롬복이 제공하는 애너테이션으로 final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는 역할을 한다.
//롬복의 @Getter, @Setter가 자동으로 Getter, Setter 메서드를 생성하는 것과 마찬가지로 @RequiredArgsConstructor는 자동으로 생성자를 생성한다.
// 따라서 스프링 의존성 주입 규칙에 의해 questionRepository 객체가 자동으로 주입된다.

@Controller

public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

//    3-14기존내용 하단내용으로 수정
//    @RequestMapping("/list")
//////기존//////////////////
////    public String list(Model model) {
//////Model 객체를 이용해서, view로 Data 전달
////// 데이터만 설정 가능
//////배열이거나 컬렉션인 경우 "List" 사용
//////클래스 이름의 첫자는 소문자 list
////        List<Question> questionList = this.questionService.getList();
//////    questionRepository 리포지터의 findAll 메서드를 사용하여 질문 목록 데이터인 questionList를 생성
//////        Model 객체에 "questionList" 라는 이름으로 값을 저장했다.
//////        Model 객체는 자바 클래스와 템플릿 간의 연결고리 역할을 한다.
//////        Model 객체에 값을 담아두면 템플릿에서 그 값을 사용할 수 있다.
//////        Model 객체는 따로 생성할 필요없이 컨트롤러 메서드의 매개변수로 지정하기만 하면 스프링부트가 자동으로 Model 객체를 생성한다.
////        model.addAttribute("questionList", questionList);
////        //        name으로 지정한 이름을 통해서 value를 사용해서 모델에 담음.
////
//////        여러 컨트롤러에서 같은 데이터를 모델에 공통적으로 넣을 때 데이터를 반환하는 메서드를 만들고, model.addAttribute애노테이션을 해당메서드에 사용하여코드중복제거
////        return "question_list";
////        //실제 호출될 템플릿 html
////    }
//    ////////////////////기존
////////////////3-02추가
//    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
//        Page<Question> paging = this.questionService.getList(page);
//        model.addAttribute("paging", paging);
//        return "question_list";
//    }

    //    3-14_2 기존내용 하단내용으로 수정
//검색어에 해당하는 kw 파라미터를 추가했고 디폴트값으로 빈 문자열을 설정했다.
//화면에서 입력한 검색어를 화면에 유지하기 위해 model.addAttribute("kw", kw)로 kw 값을 저장했다.
//kw 값이 파라미터로 들어오면 해당 값으로 질문 목록이 검색되어 조회될 것이다.
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,  @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }
    //    3-14_2 기존내용 하단내용으로 수정여기까지



//    질문 상세 페이지에 대한 URL 매핑
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);//3라인
        model.addAttribute("question", question);//4라인
        return "question_detail";
    }
    //3.4라인없을때
//    요청 URL http://localhost:8080/question/detail/2의 숫자 2처럼 변하는 id 값을 얻을 때에는 위와 같이 @PathVariable 애너테이션을 사용해야 한다. 이 때 @RequestMapping(value = "/question/detail/{id}") 에서 사용한 id와 @PathVariable("id")의 매개변수 이름이 동일해야 한다.
//
//위와 같이 수정하고 다시 URL을 호출하면 이번에는 404 대신 500 오류가 발생할 것이다. 왜냐하면 응답으로 리턴한 question_detail 템플릿이 없기 때문이다. 다음과 같이 question_detail.html 파일을 신규로 작성하자.

//////////////////////////////////////////////////////////////////////////
//3.4라인추가
//    QuestionController에서 QuestionService의 getQuestion 메서드를 호출하여 
//    Question 객체를 템플릿에 전달할 수 있도록 추가

//아래 URI에서 숫자부분이 @PathVariable로 처리해줄 수 있는 부분이다.
//  http://localhost:8080/api/user/1234

//Integer
//매개변수로 객체를 필요로 할 때
//기본형 값이 아닌 객체로 저장해야할 때
//객체 간 비교가 필요할 때
//기본형을(int) 객체로 다루기 위해 사용하는 클래스들을 래퍼 클래스(wrapper class)라고 한다
//Integer는 int의 레퍼클레스 라고 할 수 있습니다.
//int는 변수의 타입(data type  = 자료형 )
//자료형은 'data의 type에 따라 값이 저장될 공간의 크기와 저장 형식을 정의한 것

    @PreAuthorize("isAuthenticated()")
//3-8
//    principal 객체는 로그인을 해야만 생성되는 객체
//    principal 객체를 사용하는 메서드에 @PreAuthorize("isAuthenticated()") 애너테이션을 사용해야 한다.
//    @PreAuthorize("isAuthenticated()") 애너테이션이 붙은 메서드는 로그인이 필요한 메서드를 의미한다.
//    만약 @PreAuthorize("isAuthenticated()") 애너테이션이 적용된 메서드가 로그아웃 상태에서 호출되면 로그인 페이지로 이동된다.


    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    //  2-15  "질문 등록하기" 버튼을 통한 /question/create 요청은 GET 요청에 해당하므로 @GetMapping 애너테이션을 사용하였다. questionCreate 메서드는 question_form 템플릿을 렌더링하여 출력한다.
    //저장하기 오류남
//    question_form.html 에서 "저장하기" 버튼으로 폼을 전송하면 <form method="post">에 의해 POST 방식으로 데이터가 요청된다.
//    따라서 POST 요청을 처리할 수 있도록  컨트롤러를 수정해야 한다.
    //////////////////////////////////////////////////////////////////
//    @PostMapping("/question/create") 처음
//    @PostMapping("/question/create")
//    public String questionCreate(@RequestParam String subject, @RequestParam String content) {
////         TODO 질문을 저장한다.
//        this.questionService.create(subject, content);
////        TODO 주석문 대신 QuestionService로 질문 데이터를 저장하는 코드를 작성하였다. 이렇게 수정하고 질문을 작성하고 저장하면 잘 동작하는 것을 확인할 수 있을 것이다.것이다
//        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
//    }

//    POST 방식으로 요청한 /question/create URL을 처리하기 위해 @PostMapping 애너테이션을 지정한 questionCreate 메서드를 추가했다.
//    메서드명은 @GetMapping시 사용했던 questionCreate 메서드명과 동일하게 사용할 수 있다. (단, 매개변수의 형태가 다른 경우에 가능하다. - 메서드 오버로딩)
//
//    questonCreate 메서드는 화면에서 입력한 제목(subject)과 내용(content)을 매개변수로 받는다.
//    이 때 질문 등록 템플릿에서 필드 항목으로 사용했던 subject, content의 이름과 동일하게 해야 함에 주의하자.
//
//    이제 입력으로 받은 subject, content를 사용하여 질문을 저장해야 한다.
//    일단 질문 저장은 잠시 뒤로 미루고(TODO 주석만 작성했다.) 질문이 저장되면 질문 목록 페이지로 이동하도록 했다.


    @PreAuthorize("isAuthenticated()")
    //3-8
//    로그인이 필요한 메서드들에 @PreAuthorize("isAuthenticated()") 애너테이션을 적용했다.
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }
//    questionCreate 메서드의 매개변수를 subject, content 대신 QuestionForm 객체로 변경했다. subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩 된다. 이것은 스프링 프레임워크의 바인딩 기능이다.
//
//    그리고 QuestionForm 매개변수 앞에 @Valid 애너테이션을 적용했다.
//    @Valid 애너테이션을 적용하면 QuestionForm의 @NotEmpty, @Size 등으로 설정한 검증 기능이 동작한다.
//    그리고 이어지는 BindingResult 매개변수는 @Valid 애너테이션으로 인해 검증이 수행된 결과를 의미하는 객체이다.
//
//    BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다.
//    만약 2개의 매개변수의 위치가 정확하지 않다면 @Valid만 적용이 되어 입력값 검증 실패 시 400 오류가 발생한다.
//
//    따라서 questionCreate 메서드는 bindResult.hasErrors()를 호출하여 오류가 있는 경우에는 다시 폼을 작성하는 화면을 렌더링하게 했고 오류가 없을 경우에만 질문 등록이 진행되도록 했다.
//
//    여기까지 수정하고 질문 등록 화면에서 아무런 값도 입력하지 말고 "저장하기" 버튼을 눌러보자. 아무런 입력값도 입력하지 않았기 때문에 QuestionForm의 @NotEmpty에 의해 Validation이 실패하여 다시 질문 등록 화면에 머물러 있을 것이다. 하지만 QuestionForm에 설정한 "제목은 필수항목입니다." 와 같은 오류 메시지는 보이지 않는다.
//
//    오류메시지가 보이지 않는다면 어떤 항목에서 검증이 실패했는지 알 수가 없다.
//    question_form.html 메세지표시 위해서 템플릿수정해야한다

    //3-10추가
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }
//3-10추가
//    questionModify 메서드를 추가했다.
//    만약 현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우에는 "수정권한이 없습니다." 오류가 발생하도록 했다.
//    그리고 수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에 값을 담아서 템플릿으로 전달했다.
//    (이 과정이 없다면 화면에 "제목", "내용"의 값이 채워지지 않아 비워져 보인다.)
// 질문 등록시 사용했던 "question_form" 템플릿을 질문 수정에서도 사용한다는 점이다.
// 질문 등록 템플릿을 그대로 사용할 경우 질문을 수정하고 "저장하기" 버튼을 누르면 질문이 수정되는 것이 아니라 새로운 질문이 등록된다.
// 이 문제는 템플릿 폼 태그의 action을 잘 활용하면 유연하게 대처할수 있다.

    //3-10추가_1
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
//    POST 형식의 /question/modify/{id} 요청을 처리하기 위해 questionModify 메서드를 추가했다.
//    questionForm의 데이터를 검증하고 로그인한 사용자와 수정하려는 질문의 작성자가 동일한지도 검증한다.
//    검증이 통과되면 QuestionService에서 작성한 modify 메서드를 호출하여 질문 데이터를 수정한다.
//    그리고 수정이 완료되면 질문 상세 화면을 다시 호출한다. %s는 반환문자열


//   3-10_2  @{|/question/delete/${question.id}|} URL을 처리하기 위한 기능
//    URL로 전달받은 id값을 사용하여 Question 데이터를 조회한후 로그인한 사용자와 질문 작성자가 동일할 경우 위에서 작성한 서비스의 delete 메서드로 질문을 삭제한다.
//    질문 데이터 삭제후에는 질문 목록 화면으로 돌아갈 수 있도록 리스트 페이지로 리다이렉트한다.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/question/list";
    }

//    3-11 추천 버튼을 눌렀을때 호출되는 URL을 처리
//    questionVote 메서드를 추가했다.
//    추천은 로그인한 사람만 가능해야 하므로 @PreAuthorize("isAuthenticated()") 애너테이션이 적용되었다.
//    위에서 작성한 QuestionService의 vote 메서드를 호출하여 추천인을 저장했다.
//    오류가 없다면 질문 상세화면으로 리다이렉트 한다.

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }


}




////초기기본/////////////////////////////////////////////////////////////
 //스프링의 의존성 주입(Dependency Injection) 방식 3가지
//@Autowired 속성 - 속성에 @Autowired 애너테이션을 적용하여 객체를 주입하는 방식
//        생성자 - 생성자를 작성하여 객체를 주입하는 방식 (권장하는 방식)
////        Setter - Setter 메서드를 작성하여 객체를 주입하는 방식 (메서드에 @Autowired 애너테이션 적용이 필요하다.)
