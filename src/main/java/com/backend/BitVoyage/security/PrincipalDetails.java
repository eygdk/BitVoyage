package com.backend.BitVoyage.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.backend.BitVoyage.domain.User;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;

    // OAuth2User를 위한 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String) attributes.getOrDefault("name", "");
    }

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // 예시로 비어있는 리스트 반환
    }

    @Override
    public String getPassword() {
        return null;  // password 필드가 없으므로 null 반환
    }

    @Override
    public String getUsername() {
        return user.getProviderId();  // providerId를 username으로 사용
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 크리덴셜 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true;  // 계정 활성화 여부
    }

//    @Controller
//    public class LoginController{
//        private final LoginService login Service;
//
//        public LoginController(LoginService loginService){
//            this.loginService = loginService;
//        }
//
//    @RequestMapping("/login")
//    public String loginPage(){
//        return "login";
//    }
//        @RequestMapping("/kakao-login")
//        public String kakaoLoing(@RequestParam(value = "code", required = false)String code){
//            if(code!=null){
//                System.out.println("code = " + code);
//            }
//            return "redirectPage";
//        }
//    }
//===========================================
//    #login.html
//    <html xmlns:th="http://www.thymeleaf.org">
//    <head>
//        <meta charset="UTF-8">
//        <title>Title</title>
//    </head>
//    <body>
//    <a th:href="@{https://kauth.kakao.com/oauth/authorize?client_id=76962c7a6fc66be998adb93c765fc091&rediret_uri=http://localhost:3000/kakao-login&reponse_type=p70nVGMvEeg6oBCmCgGUHSJTLUqX5DZo}">
//    카카오 로그인
//    </body>
//    </html>
// ======================================
//   위의 코드를 작성한 링크
//    https://yanoos.tistory.com/103
//    ==========================
//    LoginService.java

}


