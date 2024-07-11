package com.courseshare.contentService.service;

import java.util.List;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.model.UserContentDetails;

public interface ContentService {

    public List<CourseDisplayModel> getALL() ;

    public CourseEntity add(CourseEntity course);

    public UserContentDetails getDetail(String cid,long uid);

}
