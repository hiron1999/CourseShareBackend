package com.courseshare.contentService.reposetory;

import com.courseshare.contentService.entity.CourseActivityStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourceActivityStatusReposetory extends MongoRepository<CourseActivityStatus,String> {
}
