package com.example.AwsService.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class UseDate {

    @Column(name = "created_date", updatable = false)
    protected LocalDateTime createdDate;

    @Column(name = "modified_date")
    protected LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        this.setCreatedDate(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setModifiedDate(LocalDateTime.now());
    }


}
