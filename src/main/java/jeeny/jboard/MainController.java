package jeeny.jboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/sbb")
    @ResponseBody
    //만약 @ResponseBody 애너테이션을 생략한다면 "index"라는 이름의 템플릿 파일을 찾게됨
    public String index() {
        return "안녕하세요";
    }

    @RequestMapping("/home")
    public String test() {
        return "home_view";
    }

//    @RequestMapping("/")
//    public String root() {
//        return "redirect:/question/list";
//    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/home";
    }
}


//    MainController 클래스에 @Controller 애너테이션을 적용하면 MainController 클래스는 스프링부트의 컨트롤러가 된다. 그리고 메서드의 @RequestMapping 애너테이션은 요청된 URL과의 매핑을 담당한다.
//    서버에 요청이 발생하면 스프링부트는 요청 페이지와 매핑되는 메서드를 컨트롤러를 대상으로 찾는다.
//    즉, 스프링부트는 http://localhost:8080/sbb 요청이 발생하면 /sbb URL과 매핑되는 index 메서드를 MainController 클래스에서 찾아 실행한다.
// 응답으로 "index"라는 문자열을 브라우저에 출력하기 위해 index 함수의 리턴값을 String으로 변경하고 "index"라는 문자열을 리턴했다. @ResponseBody 애너테이션은 URL 요청에 대한 응답으로 문자열을 리턴하라는 의미이다.

//    root 메서드를 추가하고 / URL을 매핑했다.
//    리턴 문자열 redirect:/question/list는 /question/list URL로 페이지를 리다이렉트 하라는 명령어이다.

//        redirect:<URL> - URL로 리다이렉트 (리다이렉트는 완전히 새로운 URL로 요청이 된다.)
//        forward:<URL> - URL로 포워드 (포워드는 기존 요청 값들이 유지된 상태로 URL이 전환된다.)