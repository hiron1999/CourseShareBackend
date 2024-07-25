package com.courseshare.AutorizationService.controller;

import com.courseshare.AutorizationService.dto.AuthRequest;
import com.courseshare.AutorizationService.dto.AuthResponse;
import com.courseshare.AutorizationService.dto.UserRegistrationRequest;
import com.courseshare.AutorizationService.entity.UserCredential;
import com.courseshare.AutorizationService.service.CustomAuthInterface;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/autentication")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
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
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest ){

      AuthResponse response = authservice.generateToken(authRequest);



      if(response!= null){
           ResponseCookie cookie = ResponseCookie.from("Refresh_Token")
                   .value(response.getRefershToken())
                   .httpOnly(true)
                   .sameSite("None")
                   .secure(true)
                   .path("/auth")
                   .build();
//                   new Cookie("Refresh_Token",response.getRefershToken());
//           cookie.setHttpOnly(true);
//           cookie.setPath("/");
//           cookie.setDomain("localhost");
//           httpResponse.addCookie(cookie);
          return ResponseEntity
                  .ok()
                  .header(HttpHeaders.SET_COOKIE,cookie.toString())
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
    ResponseEntity<?> getbyRefreshtoken(@CookieValue(name = "Refresh_Token")String refreshToken){
        log.info("cookie value : ",refreshToken);
        AuthResponse response = authservice.getbyRefreshToken(refreshToken);
       if(response!= null){
           ResponseCookie cookie = ResponseCookie.from("Refresh_Token")
                   .value(response.getRefershToken())
                   .httpOnly(true)
                   .sameSite("None")
                   .secure(true)
                   .path("/auth")
                   .build();
          return ResponseEntity
                  .ok()
                  .header(HttpHeaders.SET_COOKIE ,cookie.toString())
                  .body(response);
      }else{
          return ResponseEntity
                  .status(HttpStatus.SC_UNAUTHORIZED)
                  .body("Unable to presist refresh token");
      }
    }

    @PostMapping("/invalidateToken")
    public ResponseEntity<?> logout( @RequestHeader("Authorization")String authHeader){
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            log.info("invalidating token :",token);

            if (authservice.invalidateToken(token)) {
                ResponseCookie cookie = ResponseCookie.from("Refresh_Token")
                        .value("")
                        .httpOnly(true)
                        .sameSite("None")
                        .secure(true)
                        .path("/auth")
                        .maxAge(0)
                        .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("logged out successful");
            }
        }
        return (ResponseEntity<?>) ResponseEntity.internalServerError();
    }

}
