package com.courseshare.subscriptionService.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionRequest {
    @NonNull
    @JsonProperty(value = "UserId",required = true)
    private long userId;
    @NotNull
    @JsonProperty(value = "CourseId",required = true)
    private String courseId;
}
