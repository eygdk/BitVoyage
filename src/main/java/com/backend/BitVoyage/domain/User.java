package com.backend.BitVoyage.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // UserRole enum을 문자열로 저장하기 위해 사용
    private UserRole role; // 사용자 역할

    @Column(nullable = false)
    private String provider; // 로그인 제공자 (ex: Google, Naver)

    @Column(nullable = false)
    private String providerId; // 제공자 ID

    @Column(nullable = false)
    private String email; // 이메일

    @Column(nullable = false)
    private String name; // 이름

    private String picture; // 프로필 사진 URL

    // 추가 필드나 메소드가 필요하다면 여기에 작성
}
