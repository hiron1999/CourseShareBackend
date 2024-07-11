package com.courseshare.contentService.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("COURSE")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseEntity {
    @Id
    private String id;

    private String name;
    private String description ;
    private String author;


    private List<Module> modules;


}
