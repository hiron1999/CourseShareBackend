package com.courseshare.contentService.reposetory;

import com.courseshare.contentService.entity.Module;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModuleReposetory extends MongoRepository<Module,String> {
}
