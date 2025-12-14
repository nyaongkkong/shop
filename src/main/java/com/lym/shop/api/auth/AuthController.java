package com.lym.shop.api.auth;

import com.lym.shop.api.auth.dto.*;
import com.lym.shop.config.JwtTokenProvider;
import com.lym.shop.domain.auth.RedisRefreshTokenService;
import com.lym.shop.domain.member.Member;
import com.lym.shop.domain.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RedisRefreshTokenService redisRefreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        redisRefreshTokenService.save(request.username(), refreshToken, Duration.ofDays(7));

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, "Bearer"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        String username = authentication.getName();

        redisRefreshTokenService.delete(username);

        return ResponseEntity.ok("logout success");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        Member member = memberService.signup(
                request.username(),
                request.password(),
                request.nickname()
        );

        return ResponseEntity.ok("회원가입 성공! id=" + member.getId());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();

        // 1) 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        // 2) 타입 확인 (refresh 인지)
        String type = jwtTokenProvider.getTokenType(refreshToken);
        if (!"refresh".equals(type)) {
            return ResponseEntity.status(401).build();
        }

        // 3) username 추출
        String username = jwtTokenProvider.getUsername(refreshToken);

        // 4) Redis에서 username 으로 저장된 refresh token 꺼내서 비교
        String storedToken = redisRefreshTokenService.get(username);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        // 5) 새 access token 발급
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

        return ResponseEntity.ok(new LoginResponse(newAccessToken, refreshToken, "Bearer"));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

        return ResponseEntity.ok(new MeResponse(member.getUsername(), member.getNickname()));
    }
}
