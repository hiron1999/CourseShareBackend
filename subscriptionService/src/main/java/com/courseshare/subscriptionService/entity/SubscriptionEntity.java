package com.courseshare.subscriptionService.entity;



import java.sql.Date;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "SUBCRIPTION")
//@Table(name = "SUBCRIPTION", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "courseId"})})
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subscription_id;
    private long userId;
    private Date startDate;
    private String status;
    private String courseId;
    private String courseActivityId;
}
