package com.courseshare.contentService.service;

import com.courseshare.contentService.entity.Activity;
import com.courseshare.contentService.entity.CourseActivityStatus;
import com.courseshare.contentService.model.ActivityRequest;

public interface ContentActivityService {

    public String initiate(String courceId);
    public CourseActivityStatus get(String activityId);
    public CourseActivityStatus update(ActivityRequest activity, String courseActivityid);
}
