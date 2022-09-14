package jeeny.jboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller//@Controller 애너테이션은 HelloController 클래스가 컨트롤러의 기능을 수행한다는 의미이다.
//이 애너테이션이 있어야 스프링부트 프레임워크가 컨트롤러로 인식한다.
public class HelloController {
    @RequestMapping("/hello")
//    @RequestMapping("/hello") 애너테이션은 http://localhost:8080/hello URL 요청이 발생하면 hello 메서드가 실행됨을 의미한다. 즉, /hello URL과 hello 메서드를 매핑하는 역할을 한다.
//URL명과 메서드명은 동일할 필요는 없다. 즉 /hello URL일 때 메서드명을 hello가 아닌 hello2와 같이 해도 상관없다.

    @ResponseBody//화면출력
    public String hello() {//hello 메서드
        return "Hello Jboarddd";
    }
}
///hello URL이 요청되면 컨트롤러인 HelloController의 hello 메서드와 매핑된 hello 메서드가 호출되고 "Hello World"라는 문자열이 브라우저에 출력되는 것을 확인할 수 있다.
//public class HelloController {
//    @RequestMapping("/")
////    @RequestMapping("/hello") 애너테이션은 http://localhost:8080/hello URL 요청이 발생하면 hello 메서드가 실행됨을 의미한다. 즉, /hello URL과 hello 메서드를 매핑하는 역할을 한다.
////URL명과 메서드명은 동일할 필요는 없다. 즉 /hello URL일 때 메서드명을 hello가 아닌 hello2와 같이 해도 상관없다.
//
//
//    public String hello() {//hello 메서드
//        return "home_view";
//    }
//}