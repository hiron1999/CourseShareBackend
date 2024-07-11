package com.example.AwsService.entity;


import com.example.AwsService.entity.common.UseDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AWS_RESOURCE")
public class AWSResourceEntity extends UseDate {

    @Id
    @Column(name = "resource_id")
    private long resourceId;
    private String type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storeId")
    private DataStore dataStore;

    private String status;


    @OneToOne
    @MapsId
    @JoinColumn(name = "resource_id")
    private MappingResourceEntity mappingResourceEntity;


}
