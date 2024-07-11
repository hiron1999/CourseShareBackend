package com.example.AwsService.reposetory;

import com.example.AwsService.entity.MappingResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MappingResourceRepo extends JpaRepository<MappingResourceEntity,Long> {


    Optional<MappingResourceEntity> findBycourceResourceId(String courseId);
}
