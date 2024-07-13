package com.courseshare.contentService.controller;

import java.util.List;
import java.util.Optional;

import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.model.UserContentDetails;
import com.courseshare.contentService.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/course")
public class ContentController {

    private static final int DEFAULT_UID = 99999;
    @Autowired
    private ContentService contentService;

    @GetMapping("/")
    public List<CourseDisplayModel> getCourses(@RequestParam(name = "search_key", required = false)Optional<String> key){
        return contentService.getALL(key);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourceById(@PathVariable String id, @RequestParam("userId") Optional<Long> userId){
        UserContentDetails response;
        if(userId.isPresent()){
           response= contentService.getDetail(id,userId.get());
        }else{
          response =  contentService.getDetail(id, DEFAULT_UID);

        }
      return ResponseEntity.ok(response);
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
