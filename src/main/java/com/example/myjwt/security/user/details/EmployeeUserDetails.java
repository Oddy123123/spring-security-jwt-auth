package com.example.myjwt.security.user.details;

import com.example.myjwt.entity.Role;
import com.example.myjwt.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class EmployeeUserDetails implements UserDetails {

    private final User user;

    public EmployeeUserDetails(User user){
          this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return convertRolesToAuthorities(user.getRoles());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public User getUser(){
        return user;
    }

    private Collection<? extends GrantedAuthority> convertRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }
}
