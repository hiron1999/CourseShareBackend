package com.courseshare.contentService.model;

import com.courseshare.contentService.entity.CourseActivityStatus;
import com.courseshare.contentService.entity.CourseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserContentDetails {
    private long subscriptionID;
    private String subscriptionStatus;
    private CourseEntity courseEntity;
    private String activityStatusId;

}
