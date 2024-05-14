package com.courseshare.subscriptionService.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import com.courseshare.subscriptionService.constant.SubscriptionSpecificConstants;
import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import com.courseshare.subscriptionService.reposetory.SubscriptionReposetory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
   private SubscriptionReposetory subscriptionReposetory;

    @Override
    public SubscriptionEntity getById(long id) {
        return subscriptionReposetory.findById(id).orElseThrow();
    }

    @Override
    public List<SubscriptionEntity> getAll() {
        return subscriptionReposetory.findAll();
    }

    @Override
    public List<SubscriptionEntity> getUserSubscrition(long userId) {
        return subscriptionReposetory.findByUserId(userId);
    }

    @Override
    public SubscriptionEntity addUserSubscription(SubscriptionRequest subscriptionRequest) {

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        BeanUtils.copyProperties(subscriptionRequest,subscriptionEntity);
        subscriptionEntity.setStatus(SubscriptionSpecificConstants.SUBSCRIPTION_STATUS_ACTIVE);
        subscriptionEntity.setStartDate(Date.valueOf(LocalDate.now()));
        return subscriptionReposetory.save(subscriptionEntity);
    }



    @Override
    public SubscriptionEntity updateSubscription(SubscriptionEntity subscription) {
       if( subscriptionReposetory.findById(subscription.getSubscription_id()).isPresent() ){
           subscriptionReposetory.save(subscription);
           return subscription;
       }else {
           throw new RuntimeException("Subscrition not found!");
       }
    }
}
