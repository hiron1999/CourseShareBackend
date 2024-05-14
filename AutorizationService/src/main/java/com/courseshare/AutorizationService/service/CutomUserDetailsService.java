package com.courseshare.AutorizationService.service;

import java.util.Optional;

import com.courseshare.AutorizationService.config.CustomUserDetails;
import com.courseshare.AutorizationService.entity.UserCredential;
import com.courseshare.AutorizationService.reposetory.UserCredentialReposetory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CutomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialReposetory reposetory;
    @Override
    public UserDetails loadUserByUsername(String emailid) throws UsernameNotFoundException {
        Optional<UserCredential> credential=reposetory.findByEmailId(emailid);

        return credential.map(c->new CustomUserDetails(c.getEmailId(),c.getPassword()))
                .orElseThrow(()->new UsernameNotFoundException(String.format("%s is not a registered email",emailid)));
    }
}
