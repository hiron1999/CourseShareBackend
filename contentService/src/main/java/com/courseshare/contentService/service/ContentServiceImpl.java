package com.courseshare.contentService.service;

import java.util.List;
import java.util.stream.Collectors;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.entity.Module;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.reposetory.CourseReposetory;
import com.courseshare.contentService.reposetory.ModuleReposetory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    private CourseReposetory reposetory;
    @Autowired
    private ModuleReposetory moduleReposetory;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ContentServiceImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }

    @Override
    public List<CourseDisplayModel> getALL() {
//        return reposetory.findAll();
        Query query = new Query();
        query.fields().exclude("modules");
        List<CourseEntity> courseEntities= mongoTemplate.find(query,CourseEntity.class);


        return courseEntities.stream().map(course ->{
            CourseDisplayModel model =new CourseDisplayModel();
             BeanUtils.copyProperties(course,model);
             return model;
        }).collect(Collectors.toList());
    }

    @Override
    public CourseEntity add(CourseEntity course) {
        if (course.getModules() != null) {
            for (Module module : course.getModules()) {
                if (module.getId() == null) {
                    // Save the module to generate an ID
                    moduleReposetory.save(module);
                }
            }
        }
        return reposetory.save(course);
    }

    @Override
    public CourseEntity getDetail(String id) {
        return reposetory.findById(id).orElseThrow();
    }


}
