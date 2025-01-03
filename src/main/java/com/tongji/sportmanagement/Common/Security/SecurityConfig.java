package com.tongji.sportmanagement.Common.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Autowired
//    private UserMapper userMapper;

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(CsrfConfigurer::disable).cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(List.of("*"));
            corsConfiguration.setAllowedMethods(List.of("*"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            return corsConfiguration;
        })).authorizeHttpRequests(authorize -> authorize.requestMatchers(authorizedRoutes().toArray(new String[0])).authenticated().anyRequest().permitAll()).formLogin(FormLoginConfigurer::disable).httpBasic(HttpBasicConfigurer::disable).addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> new CJUserDetails(new User(1, "wfy3364", "123456")); //改成getUserByUserName
//    }

    // 在这里定义需要认证的api
    @Bean
    public List<String> authorizedRoutes() {
        return List.of("/api/users/authorTest", "/api/users/info", "/api/users/password", "/api/reservations/**");
//        return List.of();
    }

    // 使用in memory实现的token失效管理器
    @Bean
    public InvalidTokenManager invalidTokenManager() {
        return new InMemoryInvalidTokenManager();
    }
}
