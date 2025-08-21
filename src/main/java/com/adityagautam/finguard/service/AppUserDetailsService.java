package com.adityagautam.finguard.service;

import com.adityagautam.finguard.model.Profile;
import com.adityagautam.finguard.repository.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final ProfileRepo profileRepo;
    @Override
    public UserDetails loadUserByUsername(String email){
        Profile existingUser = profileRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Profile not found with email: "+email));
        return User.builder()
                .username(existingUser.getEmail())
                .password(existingUser.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
