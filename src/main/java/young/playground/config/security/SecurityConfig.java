package young.playground.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable()) // 쿠키/세션 기반이 아니라면 보통 비활성화
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 무상태
        .cors(Customizer.withDefaults()) // CORS 정책 별도 설정해두기
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST,"/signup").permitAll()
            .anyRequest().authenticated()
        )
        .httpBasic(hb -> hb.disable()) // 기본 Basic 인증/폼 로그인 끄기
        .formLogin(f -> f.disable())
        // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 필터 연결
        .build();
  }
}
