package com.courseshare.contentService.service;

import java.util.List;
import java.util.Optional;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.model.UserContentDetails;

public interface ContentService {

    public List<CourseDisplayModel> getALL(Optional<String>key) ;

    public CourseEntity add(CourseEntity course);

    public UserContentDetails getDetail(String cid,long uid);

}
