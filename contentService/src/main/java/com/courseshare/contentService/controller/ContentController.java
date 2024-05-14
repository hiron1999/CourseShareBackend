package com.courseshare.contentService.controller;

import java.util.List;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/course")
public class ContentController {

    @Autowired
    ContentService contentService;

    @GetMapping("/")
    public List<CourseDisplayModel> getCourses(){
        return contentService.getALL();
    }

    @GetMapping("/{id}")
    public CourseEntity getCourceById(@PathVariable String id){
        return contentService.getDetail( id);
    }
    @PostMapping("/")
    public CourseEntity addCourse(@RequestBody CourseEntity course){
        return contentService.add(course);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<CourseEntity> updateCourse(@PathVariable String id,){
//
//        contentService
//
//    }

}
