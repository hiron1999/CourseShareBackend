package com.courseshare.contentService.service;

import java.util.List;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.model.CourseDisplayModel;

public interface ContentService {

    public List<CourseDisplayModel> getALL() ;

    public CourseEntity add(CourseEntity course);

    CourseEntity getDetail(String id);
}
