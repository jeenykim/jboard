package jeeny.jboard.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;
}

//엔티티명을 User 대신 SiteUser로 한 이유는 스프링 시큐리티에 이미 User 클래스가 있기 때문이다.
//  username, email 속성에는 @Column(unique = true) 처럼 unique = true를 지정했다.
//unique = true는 유일한 값만 저장할 수 있음을 의미한다.
//즉, 값을 중복되게 저장할 수 없음을 뜻한다.
//이렇게 해야 username과 email에 동일한 값이 저장되지 않는다.