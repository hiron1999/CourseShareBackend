package com.example.AwsService.reposetory;

import com.example.AwsService.entity.DataStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataStoreRepo extends JpaRepository<DataStore,String> {
}
