package com.courseshare.contentService.controller;

import com.courseshare.contentService.entity.Activity;
import com.courseshare.contentService.entity.ActivityDetails;
import com.courseshare.contentService.entity.CourseActivityStatus;
import com.courseshare.contentService.model.ActivityRequest;
import com.courseshare.contentService.service.ContentActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Activity")
public class ActivityContentController {

    @Autowired
    private ContentActivityService activityService;


    @PostMapping("/generate/")
    public String initilizeActivityStatus(@RequestBody String courceId){
        return activityService.initiate(courceId);
    }

    @GetMapping("/{id}")
    private CourseActivityStatus getActivityDetails(@PathVariable String id){
        return activityService.get(id);
    }

    @PostMapping("/{statusId}/update")
    public ResponseEntity<?> updateActivity(@RequestBody ActivityRequest request, @PathVariable String statusId){
        return ResponseEntity.ok().body(activityService.update(request,statusId));
    }
}
