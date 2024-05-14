package com.courseshare.contentService.reposetory;

import com.courseshare.contentService.entity.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseReposetory extends MongoRepository<CourseEntity,String> {
}
