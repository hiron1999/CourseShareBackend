package com.courseshare.contentService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;




@AllArgsConstructor
@Data
public class Leacture {
    @Id
    private String id;
    private String title;
    private String path;

    public Leacture(){
        id = new ObjectId().toString();
    }

}
