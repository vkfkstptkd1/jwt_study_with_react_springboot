package thekary.jwt_study.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //스프링 부트 내부 메모리에 저장
    @Bean
    public InMemoryUserDetailsManager user() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password("{noop}1234")
                        .authorities("read") // 권한설정
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // 위조 요청 방지 ,, html에서 특정 토큰이 있어여
                .authorizeRequests( auth -> auth.anyRequest().authenticated()) //http 요청 접근 제한 설정.
                .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 생성 X JWT 토큰 쓸 때 사용.
                .httpBasic(withDefaults())
                .build();
//        http.csrf().ignoringAntMatchers("/callBackPush/**")//csrf예외처리
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());//csrf 토큰자동생성
    }
}
