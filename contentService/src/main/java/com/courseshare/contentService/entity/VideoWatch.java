package com.courseshare.contentService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoWatch extends ActivityDetails{

    private String leactureId;
    private double playedTime;
    private boolean isCompleted;
}
