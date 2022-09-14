package jeeny.jboard.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


    @RequiredArgsConstructor
    @Service
    public class UserSecurityService implements UserDetailsService {

        private final UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            스프링 시큐리티에 등록하여 사용할 UserSecurityService는 스프링 시큐리티가 제공하는 UserDetailsService 인터페이스를 구현(implements)해야 한다.
//            loadUserByUsername 메서드를 구현하도록 강제하는 인터페이스이다.
//            loadUserByUsername 메서드는 사용자명으로 비밀번호를 조회하여 리턴하는 메서드이다.

            Optional<SiteUser> _siteUser = this.userRepository.findByusername(username);
            if (_siteUser.isEmpty()) {
                throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
            }
//            loadUserByUsername 메서드는 사용자명으로 SiteUser 객체를 조회하고 만약 사용자명에 해당하는 데이터가 없을 경우에는 UsernameNotFoundException 오류를 내게 했다.

            SiteUser siteUser = _siteUser.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if ("admin".equals(username)) {
//                사용자명이 "admin"인 경우에는 ADMIN 권한을 부여하고 그 이외의 경우에는 USER 권한을 부여했다.
                        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            } else {
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
//                그 이외의 경우에는 USER 권한을 부여했다.
            }
            return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
//            스프링 시큐리티는 loadUserByUsername 메서드에 의해 리턴된
//            User 객체의 비밀번호가 화면으로부터 입력 받은 비밀번호와 일치하는지를 검사하는 로직을 내부적으로 가지고 있다.
        }
}
