package com.lym.shop.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // PK

    @Column(nullable = false, length = 50, unique = true)
    private String email;       // 로그인 ID 용

    @Column(nullable = false, length = 20)
    private String name;        // 이름, 닉네임 등

    @Column(nullable = false, length = 100)
    private String password;    // 암호화된 비밀번호

    @Column(nullable = false, length = 20)
    private String role;        // USER, ADMIN 등

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 가입일시

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일시

    // 편의 메서드 예시
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }
}
