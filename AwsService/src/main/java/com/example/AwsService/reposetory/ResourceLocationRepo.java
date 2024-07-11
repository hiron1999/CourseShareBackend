package com.example.AwsService.reposetory;

import com.example.AwsService.entity.ResourceLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceLocationRepo extends JpaRepository<ResourceLocation,Long> {
}
