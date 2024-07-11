package com.courseshare.contentService.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@Data
public class Activity {
    @Id
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;
    @JsonProperty(required = true)
    private ActivityDetails activityDetails;

    public Activity(){
        id=new ObjectId().toString();
        timeStamp =LocalDateTime.now();
    }
}
