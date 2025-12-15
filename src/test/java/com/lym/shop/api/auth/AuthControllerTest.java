package com.lym.shop.api.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lym.shop.api.auth.dto.LoginRequest;
import com.lym.shop.config.JwtTokenProvider;
import com.lym.shop.domain.auth.RedisRefreshTokenService;
import com.lym.shop.domain.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    AuthenticationManager authenticationManager;

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    MemberService memberService;

    @MockitoBean
    RedisRefreshTokenService redisRefreshTokenService;

    @Test
    @DisplayName("로그인 성공 시 accessToken, refreshToken을 반환한다")
    void login_success() throws Exception {
        // ===== GIVEN =====
        LoginRequest request = new LoginRequest("dlals711@naver.com", "dldudals00!");

        var authentication = mock(org.springframework.security.core.Authentication.class);

        given(authenticationManager.authenticate(any()))
                .willReturn(authentication);

        given(jwtTokenProvider.generateAccessToken(authentication))
                .willReturn("access-token");
        given(jwtTokenProvider.generateRefreshToken(authentication))
                .willReturn("refresh-token");

        // ===== WHEN & THEN =====
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("로그인 실패 시 401과 에러 메시지를 반환한다")
    void login_fail_bad_credentials() throws Exception {
        // ===== GIVEN =====
        LoginRequest request = new LoginRequest("test", "wrong");

        given(authenticationManager.authenticate(any()))
                .willThrow(new org.springframework.security.authentication.BadCredentialsException("bad"));

        // ===== WHEN & THEN =====
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message")
                        .value("아이디 또는 비밀번호가 올바르지 않습니다."));
    }

    @Test
    @DisplayName("로그인 요청값이 비어있으면 validation 에러가 발생한다")
    void login_validation_fail() throws Exception {
        // ===== GIVEN =====
        LoginRequest request = new LoginRequest("", "");

        // ===== WHEN & THEN =====
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"));
    }

}
