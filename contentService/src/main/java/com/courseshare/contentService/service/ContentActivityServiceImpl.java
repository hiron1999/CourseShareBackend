package com.courseshare.contentService.service;

import com.courseshare.contentService.entity.Activity;
import com.courseshare.contentService.entity.CourseActivityStatus;
import com.courseshare.contentService.model.ActivityRequest;
import com.courseshare.contentService.reposetory.CourceActivityStatusReposetory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContentActivityServiceImpl implements ContentActivityService{

    @Autowired
    private CourceActivityStatusReposetory activityStatusReposetory;


    @Override
    public String initiate(String courceId) {
        CourseActivityStatus activityStatus
               = CourseActivityStatus.builder()
                .courseId(courceId)
                .build();
        activityStatus = activityStatusReposetory.save(activityStatus);

        return activityStatus.getId();
    }

    @Override
    public CourseActivityStatus get(String activityId) {
        var activityStatus= activityStatusReposetory.findById(activityId);

        if(!activityStatus.isPresent()){
            throw new RuntimeException("no activity found");
        }

        return shortActivitybyDate(activityStatus.get());

    }

    @Override
    public CourseActivityStatus update(ActivityRequest activity, String id) {
        CourseActivityStatus activityStatus =
                activityStatusReposetory.findById(id)
                        .orElseThrow(() -> new RuntimeException("ActivityStatus not found with ID: " + id));
        if(activityStatus.getActivityList()==null){
            activityStatus.setActivityList(new ArrayList<>());
        }
        List<Activity> activityList= addorupdateActivity(activityStatus.getActivityList(),activity);

        activityStatus.setActivityList(activityList);
        return shortActivitybyDate(activityStatusReposetory.save(activityStatus));
    }

    private List<Activity> addorupdateActivity(List<Activity> activityList, ActivityRequest newActivity) {

        boolean isfound = false;
        for(Activity activity : activityList){
            if(activity.getId().equals(newActivity.getId())){
                BeanUtils.copyProperties(newActivity,activity);
                activity.setTimeStamp(LocalDateTime.now());
                isfound=true;
            }

        }
        if (!isfound){
            Activity activity =new Activity();
            activity.setActivityDetails(newActivity.getActivityDetails());
//            activity.setTimeStamp(LocalDateTime.now());

            activityList.add(activity);
        }
        return activityList;
    }

    public CourseActivityStatus shortActivitybyDate(CourseActivityStatus activityStatus){
        Comparator<Activity> bydate=Comparator.comparing(Activity::getTimeStamp).reversed();
        activityStatus.getActivityList().sort(bydate);

        return activityStatus;
    }
}
