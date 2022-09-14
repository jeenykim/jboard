package jeeny.jboard;

import jeeny.jboard.user.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
//@Configuration은 스프링의 환경설정 파일임을 의미하는 애너테이션이다.
// 여기서는 스프링 시큐리티의 설정을 위해 사용되었다.
@EnableWebSecurity
//@EnableWebSecurity는 모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션이다.
//@EnableWebSecurity 애너테이션을 사용하면 내부적으로 SpringSecurityFilterChain이 동작하여 URL 필터가 적용된다.

//3-8
//SecurityConfig에 적용한@EnableGlobalMethodSecurity 애너테이션의 prePostEnabled = true 설정은 QuestionController와 AnswerController에서 로그인 여부를 판별하기 위해 사용했던 @PreAuthorize 애너테이션을 사용하기 위해 반드시 필요하다.
//로그인 하지 않은 상태에서 "질문 등록" 버튼을 누르면 "로그인" 화면으로 이동한다.
// 그리고 로그인을 진행하면 원래 하려고 했던 "질문 등록" 화면으로 이동한다.
// 이것은 로그인 후에 원래 하려고 했던 페이지로 리다이렉트 시키는 스프링 시큐리티의 기능이다.
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    @Bean
//    스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정할 수 있다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                //http.authorizeRequests().antMatchers("/**").permitAll()은 모든 인증되지 않은 요청을 허락한다는 의미이다.
// 따라서 로그인을 하지 않더라도 모든 페이지에 접근할 수 있다.
//and() - http 객체의 설정을 이어서 할 수 있게 하는 메서드이다.
// http.csrf().ignoringAntMatchers("/h2-console/**") - /h2-console/로 시작하는 URL은 CSRF 검증을 하지 않는다는 설정이다.

                .and()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
//        X-Frame-Options 헤더는 clickjacking attack을 방지하기 위해 사용하는 헤더이다.
//        클릭재킹 공격은 일반적으로 사용자가 수행하고 있다고 생각하는 작업 위에
//        보이지 않는 페이지 요소를 렌더링하여 웹 사용자가 의도하지 않은 작업을 수행하도록 속인다.
//        ex:실제 로그인 상자 위에 가짜 로그인 상자를 렌더링하여 로그인 자격 증명을 수집 한다.

        //URL 요청시 X-Frame-Options 헤더값을 sameorigin으로 설정하여 오류가 발생하지 않도록 했다.
// X-Frame-Options 헤더의 값으로 sameorigin을 설정하면
// frame에 포함된 페이지가 페이지를 제공하는 사이트와 동일한 경우에는 계속 사용할 수 있다.

                .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/")

                //3-07추가
//        추가한 and().formLogin().loginPage("/user/login").defaultSuccessUrl("/") 은
//        스프링 시큐리티의 로그인 설정을 담당하는 부분으로
//        로그인 페이지의 URL은 /user/login이고 로그인 성공시에 이동하는 디폴트 페이지는 루트 URL(/)임을 의미한다.

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        //3-07마지막 추가
//        로그아웃을 위한 설정을 추가했다.
//        로그아웃 URL을 /user/logout으로 설정하고 로그아웃이 성공하면 루트(/) 페이지로 이동하도록 했다.
//        로그아웃시 생성된 사용자 세션도 삭제하도록 처리했다.
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    //사용자의 비밀번호는 보안을 위해 반드시 암호화하여 저장해야 한다.
//    PasswordEncoder를 @Bean으로 등록하면 PasswordEncoder 객체를 주입받아 사용가능
//PasswordEncoder는 BCryptPasswordEncoder의 인터페이스이다.
//BCryptPasswordEncoder는 BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 암호화한다.

    //3-07추가
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    AuthenticationManager 빈을 생성했다.
//    AuthenticationManager는 스프링 시큐리티의 인증을 담당한다.
//    AuthenticationManager 빈 생성시 스프링의 내부 동작으로 인해 위에서 작성한
//    UserSecurityService와 PasswordEncoder가 자동으로 설정된다.



}