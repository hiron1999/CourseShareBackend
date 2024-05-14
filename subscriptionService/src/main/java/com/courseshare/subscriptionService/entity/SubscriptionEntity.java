package com.courseshare.subscriptionService.entity;



import java.sql.Date;

import com.courseshare.subscriptionService.constant.SubscriptionSpecificConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "SUBCRIPTION")
public class SubscriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long subscription_id;
    private long userId;
    private String status;
    private String courseId;
    private Date startDate;
}
