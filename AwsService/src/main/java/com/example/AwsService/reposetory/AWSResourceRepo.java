package com.example.AwsService.reposetory;

import com.example.AwsService.entity.AWSResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AWSResourceRepo extends JpaRepository<AWSResourceEntity,Long> {
}
