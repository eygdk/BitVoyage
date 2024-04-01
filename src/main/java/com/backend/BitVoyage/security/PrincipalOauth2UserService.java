package com.backend.BitVoyage.security;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.backend.BitVoyage.domain.User;
import com.backend.BitVoyage.repository.UserRepository;
import com.backend.BitVoyage.domain.UserRole; // UserRole이 domain 패키지 안에 있다고 가정

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // Constructor for dependency injection
    public PrincipalOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");

        Optional<User> optionalUser = userRepository.findByProviderId(providerId);
        User user;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(oAuth2User.getAttribute("email")) // 이메일 속성 추가
                    .name(oAuth2User.getAttribute("name"))
                    .picture(oAuth2User.getAttribute("picture")) // 프로필 사진 URL 추가
                    .role(UserRole.USER) // 예시로 USER를 사용, 실제로는 UserRole에 정의된 값을 사용
                    .build();
            userRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
