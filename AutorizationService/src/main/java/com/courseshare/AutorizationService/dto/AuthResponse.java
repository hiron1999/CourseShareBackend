package com.courseshare.AutorizationService.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @JsonProperty("accessToken")
    private String accessToken;
    private String refershToken;
    private Date  expired_on;
    private long UserId;

}
