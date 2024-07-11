package com.example.AwsService.entity;

import com.example.AwsService.entity.common.UseDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RESOURCE_LOCATION")
public class ResourceLocation extends UseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "location_id")
    private long locationId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "storeId")
    private DataStore dataStore;

    private String path;



}
