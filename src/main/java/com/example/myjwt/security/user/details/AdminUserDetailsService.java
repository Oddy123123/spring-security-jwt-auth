package com.example.myjwt.security.user.details;

import com.example.myjwt.entity.Admin;
import com.example.myjwt.entity.User;
import com.example.myjwt.repository.AdminRepository;
import com.example.myjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if(admin == null) {
            throw new UsernameNotFoundException("Admin not found");
        }
        return new AdminUserDetails(admin);
    }
}
