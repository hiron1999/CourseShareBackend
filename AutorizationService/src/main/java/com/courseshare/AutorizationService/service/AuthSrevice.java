package com.courseshare.AutorizationService.service;

import java.util.Date;

import Model.UserModel;
import com.courseshare.AutorizationService.Utills.JwtUtill;
import com.courseshare.AutorizationService.dto.AuthRequest;
import com.courseshare.AutorizationService.dto.AuthResponse;
import com.courseshare.AutorizationService.dto.UserRegistrationRequest;
import com.courseshare.AutorizationService.entity.RefreshToken;
import com.courseshare.AutorizationService.entity.UserCredential;
import com.courseshare.AutorizationService.reposetory.UserCredentialReposetory;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class AuthSrevice implements CustomAuthInterface{
    public static final String TOKEN_STASTUS_ACTIVE = "active";
    public static final String TOKEN_STATUS_DISABLED = "disabled";
    @Autowired
    private UserCredentialReposetory userCredentialReposetory;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtill jwtUtill;
    @Override
    public ResponseEntity<?> register(UserRegistrationRequest request) {
        UserCredential credential = new UserCredential();
        credential.setEmailId(request.getEmail());
        credential.setPassword(passwordEncoder.encode(request.getPassword()));

        try{
//        check if email exist
            if(userCredentialReposetory.findByEmailId(request.getEmail()).isPresent()){
               return ResponseEntity
                        .status(HttpStatus.SC_CONFLICT)
                        .body("This email is alredy registered\n try to login");
            }

        credential= userCredentialReposetory.save(credential);
        UserModel newUser = convertToUserModel(request);
        //set user id
            newUser.setUserid(credential.getId());

        rabbitTemplate.convertAndSend("","q.user-register",newUser);
        return ResponseEntity
                        .status(HttpStatus.SC_CREATED)
                        .body("User created with Id : "+credential.getId());

        }catch (Exception e){
            // reverting user registration
            log.info("reverting user registration...........");
            userCredentialReposetory.deleteById(credential.getId());
          return ResponseEntity
                        .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                        .body(e.getMessage());
        }

    }



    @Override
    public AuthResponse generateToken(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            UserCredential user=userCredentialReposetory.findByEmailId(authRequest.getEmail()).orElseThrow();
            long id=user.getId();


            AuthResponse authResponse=generateAuth(id,authRequest.getEmail());

            String refershToken =authResponse.getRefershToken();

            updateRefreshToken(refershToken);


            return authResponse;
        } else {
            return null;
        }
    }





    @Override
    public boolean validateToken(String token) {
       return jwtUtill.validateToken(token);

    }
    public UserCredential validateRefreshtoken(String token ){
        long userId=jwtUtill.extractUserid(token);
        Boolean isExpired=jwtUtill.isTokenExpired(token);
        UserCredential user= userCredentialReposetory.findById(userId).orElseThrow();
        if (isExpired) {
            throw new RuntimeException("Refreshtoken is expired");
        } else {
            if (user.getRefreshToken().getStatus().equals(TOKEN_STASTUS_ACTIVE) && user.getRefreshToken().getToken().equals(token)) {
                return user;
            } else {

                throw new RuntimeException("Invalid refresh token");
            }
        }
    }

    @Override
    public AuthResponse getbyRefreshToken(String refreshToken) {
        UserCredential userCredential =validateRefreshtoken(refreshToken);
        AuthResponse response =generateAuth(userCredential.getId(),userCredential.getEmailId());
        updateRefreshToken(response.getRefershToken());
        return response;
    }

    @Override
    public boolean invalidateToken(String token) {
       try {
           jwtUtill.invalidateToken(token);
           return true;
       }catch (Exception e){
           log.error(e.getLocalizedMessage());
           return false;
       }

    }

    private AuthResponse generateAuth(long id, String emailId) {

        final String accessToken = jwtUtill.generateToken(emailId);
            final String refershToken = jwtUtill.generateRefreshToken(id);
            final Date expiredOn = jwtUtill.extractExpiration(refershToken);

            return new AuthResponse(
                    accessToken,
                    refershToken,
                    expiredOn,id);
    }

    //updateing refresh token in db
    public void updateRefreshToken(String refreshToken){
        UserCredential userCredential = userCredentialReposetory.findById(jwtUtill.extractUserid(refreshToken)).orElseThrow();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                    .token(refreshToken)
                    .status(TOKEN_STASTUS_ACTIVE)
                    .build();
        userCredential.setRefreshToken(refreshTokenEntity);
        userCredentialReposetory.save(userCredential);
    }


    private UserModel convertToUserModel(UserRegistrationRequest request) {
        UserModel model = new UserModel();
        String [] name =request.getName().split(" ");
        String firstname =name[0];
        String lastname = name.length > 1 ? name[name.length -1] : null;
        model.setFirst_name(firstname);
        model.setLast_name(lastname);
        model.setEmail(request.getEmail());
        model.setUser_type("User");

        return model;
    }

}
