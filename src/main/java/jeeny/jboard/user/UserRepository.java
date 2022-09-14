package jeeny.jboard.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
        Optional<SiteUser> findByusername(String username);
        //3-07로그인로그아웃추가
        // UserSecurityService는 사용자를 조회하는 기능이 필요하므로
//        findByusername 메서드를 User 리포지터리에 추가
        }

//    SiteUser의 PK의 타입은 Long이다. 따라서 JpaRepository<SiteUser, Long>처럼 사용했다.
//pk 행을 고유하게 구분해 주는 최소의 정보,하나의 primary key만 존재



