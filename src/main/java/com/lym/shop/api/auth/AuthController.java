package com.lym.shop.api.auth;

import com.lym.shop.api.auth.dto.*;
import com.lym.shop.api.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        redisRefreshTokenService.save(request.username(), refreshToken, Duration.ofDays(7));

        return ResponseEntity.ok(
                ApiResponse.ok(new LoginResponse(accessToken, refreshToken, "Bearer"))
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest request) {
        Member member = memberService.signup(
                request.username(),
                request.password(),
                request.nickname()
        );

        return ResponseEntity.ok(
                ApiResponse.ok(new SignupResponse(member.getId()))
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("refreshToken이 유효하지 않습니다.");
        }

        if (!"refresh".equals(jwtTokenProvider.getTokenType(refreshToken))) {
            throw new IllegalArgumentException("refreshToken 타입이 올바르지 않습니다.");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        String stored = redisRefreshTokenService.get(username);

        if (stored == null || !stored.equals(refreshToken)) {
            throw new IllegalArgumentException("refreshToken이 서버 저장값과 일치하지 않습니다.");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
        String newAccessToken = jwtTokenProvider.generateAccessToken(auth);

        return ResponseEntity.ok(
                ApiResponse.ok(new LoginResponse(newAccessToken, refreshToken, "Bearer"))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

        return ResponseEntity.ok(
                ApiResponse.ok(new MeResponse(member.getUsername(), member.getNickname()))
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        String username = authentication.getName();
        redisRefreshTokenService.delete(username);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
