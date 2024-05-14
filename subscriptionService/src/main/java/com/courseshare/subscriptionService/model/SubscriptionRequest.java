package com.courseshare.subscriptionService.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionRequest {
    @JsonProperty(value = "UserId",required = true)
    private long userId;
    @JsonProperty(value = "UserId",required = true)
    private String courseId;
}
