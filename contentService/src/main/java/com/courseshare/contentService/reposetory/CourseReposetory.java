package com.courseshare.contentService.reposetory;

import com.courseshare.contentService.entity.CourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseReposetory extends MongoRepository<CourseEntity,String> {
}
