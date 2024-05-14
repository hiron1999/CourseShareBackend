package com.courseshare.AutorizationService.controller;

import com.courseshare.AutorizationService.dto.AuthRequest;
import com.courseshare.AutorizationService.dto.AuthResponse;
import com.courseshare.AutorizationService.dto.UserRegistrationRequest;
import com.courseshare.AutorizationService.entity.UserCredential;
import com.courseshare.AutorizationService.service.CustomAuthInterface;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/autentication")
public class AuthController {

    @Autowired
    private CustomAuthInterface  authservice;



    @PostMapping("/register")
    public  ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest , BindingResult result ){

        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }else{



        return authservice.register(userRegistrationRequest);
        }

    }
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest , HttpServletResponse httpResponse){

      AuthResponse response = authservice.generateToken(authRequest);



      if(response!= null){
           Cookie cookie =new Cookie("Refresh_Token",response.getRefershToken());
           httpResponse.addCookie(cookie);
          return ResponseEntity
                  .ok()
                  .body(response)
                  ;
      }else{
          return ResponseEntity
                  .status(HttpStatus.SC_UNAUTHORIZED)
                  .body("invalid Credentials");
      }


    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam("token")String token){

       if( authservice.validateToken(token)){

           return ResponseEntity
                   .ok()
                   .body("Token is Valid");

       }
       else {
           return ResponseEntity
                   .status(org.apache.http.HttpStatus.SC_FORBIDDEN)
                   .body("Invalid or expaired token");
       }

    }

    @GetMapping("/v1/token")
    ResponseEntity<?> getbyRefreshtoken(@CookieValue(value = "Refresh_Token",defaultValue = "not available")String refreshToken, HttpServletResponse httpResponse){
      AuthResponse response = authservice.getbyRefreshToken(refreshToken);
       if(response!= null){
           Cookie cookie =new Cookie("Refresh_Token",response.getRefershToken());
           httpResponse.addCookie(cookie);
          return ResponseEntity
                  .ok()
                  .body(response);
      }else{
          return ResponseEntity
                  .status(HttpStatus.SC_UNAUTHORIZED)
                  .body("Unable to presist refresh token");
      }
    }

}
