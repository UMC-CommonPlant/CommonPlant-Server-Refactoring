package com.umc.commonplant.domain.user.service;

import com.umc.commonplant.domain.user.entity.User;
import com.umc.commonplant.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    //스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
    private final UserRepository userRepository;

    // 사용자(이름)email로 사용자의 정보를 가져오는 메서드
    @Override
    public User loadUserByUsername(String uuid){
        return userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException(uuid));
    }
}
