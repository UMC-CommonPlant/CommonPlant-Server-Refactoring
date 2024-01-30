package com.umc.commonplant.domain.user.entity;

import com.umc.commonplant.domain.BaseTime;
import com.umc.commonplant.domain.oauth.SocialType;
import com.umc.commonplant.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Getter
@Table(name = "user")
@NoArgsConstructor
@Entity
public class User extends BaseTime implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "img_url", nullable = true)
    private String imgUrl;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    //권한반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user"));
    }
    // 사용자 id를 반환(고유한 값)
    @Override
    public String getUsername(){return email;}
    @Override
    public boolean isAccountNonLocked(){
        // 계정 잠금되있는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    //계정 사용가능 여부 반환
    @Override
    public boolean isEnabled(){
        // 계정이 사용가능한지 확인하는 로직
        return true; // true -> 사용가능
    }


    @Builder
    public User(String name, String email, String provider, String imgUrl, String uuid){
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.imgUrl = imgUrl;
        this.uuid = uuid;
    }
    // Set Profile Image
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    // Update User name
    public User update(String name) {
        this.name = name;

        return this;
    }

}
