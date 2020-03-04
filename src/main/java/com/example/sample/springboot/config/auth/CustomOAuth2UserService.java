package com.example.sample.springboot.config.auth;

import com.example.sample.springboot.config.auth.dto.OAuthAttributes;
import com.example.sample.springboot.config.auth.dto.SessionUser;
import com.example.sample.springboot.domain.user.User;
import com.example.sample.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

//해당 클래스는 구글 로그인 이후 가져온 사용자의 정보들을 기반으로 가입, 정보수정, 세션 저장 등의 기능 지원
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate= new DefaultOAuth2UserService();
        //delegate : 대리자
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //로그인 진행 중인 서비스 구분 ID(google, naver...)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        //로그인 진행시 키가 되는 필드값(PK)(1, 2...)

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //OAuth2UserService를 통해 가져온 유저의 속성들을 담을 클래스

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); //세션에 사용자 정보 저장

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRoleKey())
                ),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
