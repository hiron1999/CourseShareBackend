package com.example.AwsService.entity;

import com.example.AwsService.entity.common.UseDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DATA_STORE")
public class DataStore extends UseDate {

    @Id
    private String storeId;
    private String bucketName;
//    private String region;
    private String type;

}
