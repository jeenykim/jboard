package jeeny.jboard.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;
//username은 필수항목이고 길이가 3-25 사이여야 한다는 검증조건을 설정했다.
// @Size는 폼 유효성 검증시 문자열의 길이가 최소길이(min)와 최대길이(max) 사이에 해당하는지를 검증한다.

//    password1과 password2는 "비밀번호"와 "비밀번호확인"에 대한 속성이다.
//
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;
//    password1과 password2는 "비밀번호"와 "비밀번호확인"에 대한 속성이다.
//    로그인 할때는 비밀번호가 한번만 필요하지만 회원가입시에는 입력한 비밀번호가 정확한지 확인하기 위해 2개의 필드가 필요하다.

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;

// email 속성에는 @Email 애너테이션이 적용되었다.
// @Email은 해당 속성의 값이 이메일형식과 일치하는지를 검증한다.

    //UserController 메서드정의
    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

