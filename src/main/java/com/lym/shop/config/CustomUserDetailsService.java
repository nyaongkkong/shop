package com.lym.shop.config;

import com.lym.shop.domain.member.Member;
import com.lym.shop.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원"));
/*
        return User.withUsername(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole().replace("ROLE_", ""))
                .build();
 */

        return new MemberDetails(member);
    }
}
