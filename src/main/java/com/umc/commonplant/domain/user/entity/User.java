package com.umc.commonplant.domain.user.entity;

import com.umc.commonplant.domain.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user"));
    }
    @Override
    public String getPassword(){
        return null;
    }
    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
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
    public User update(String name){
        this.name = name;

        return this;
    }

}
