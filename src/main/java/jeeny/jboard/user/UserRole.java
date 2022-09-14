package jeeny.jboard.user;

import lombok.Getter;

@Getter
public enum UserRole {

    //    UserRole은 열거 자료형(enum)으로 작성했다.
//    ADMIN은 "ROLE_ADMIN", USER는 "ROLE_USER" 라는 값을 가지도록 했다.
//    그리고 상수 자료형이므로 @Setter없이 @Getter만 사용가능하도록 했다.
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}

//    enum은 열거형(enumerated type)이라고 부른다.
//    열거형은 서로 연관된 상수들의 집합이라고 할 수 있다.

//    스프링 시큐리티는 인증 뿐만 아니라 권한도 관리한다.
//    따라서 인증후에 사용자에게 부여할 권한이 필요하다.
//    ADMIN, USER 2개의 권한을 갖는 UserRole을 신규로 작성하자

