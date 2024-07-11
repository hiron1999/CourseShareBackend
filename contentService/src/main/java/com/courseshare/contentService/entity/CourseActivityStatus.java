package com.courseshare.contentService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Document("COURSE_ACTIVITY_STATUS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CourseActivityStatus {
    @Id
    private String id;
    private String courseId;
    private List<Activity> activityList;
}
