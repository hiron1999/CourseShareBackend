package com.courseshare.contentService.model;

import com.courseshare.contentService.entity.ActivityDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ActivityRequest {
    private String id;

    @JsonProperty(required = true)
    private ActivityDetails activityDetails;

}
