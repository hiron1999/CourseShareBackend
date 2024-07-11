package com.example.AwsService.entity;

import com.example.AwsService.entity.common.UseDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MAPPING_RESOURCE" ,uniqueConstraints = @UniqueConstraint(columnNames = {"courceResourceId"}))
public class MappingResourceEntity extends UseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long awsResourceId;


    private String courceResourceId;

    @OneToOne(mappedBy = "mappingResourceEntity", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AWSResourceEntity awsResourceEntity;


}
