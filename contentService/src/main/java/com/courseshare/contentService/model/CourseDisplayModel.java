package com.courseshare.contentService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseDisplayModel {
     private String id;

    private String name;
    private String description ;
    private String autor;

}
