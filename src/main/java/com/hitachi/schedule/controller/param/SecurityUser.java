package com.hitachi.schedule.controller.param;

import com.hitachi.schedule.mybatis.pojo.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
public class SecurityUser extends User implements UserDetails {
    private Set<GrantedAuthority> userRoleList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getUserRoleList();
    }

    @Override
    public String getPassword() {
        return this.getUser_password();
    }

    @Override
    public String getUsername() {
        return this.getUser_id();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
