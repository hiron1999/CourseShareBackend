package com.courseshare.subscriptionService.service;

import java.util.List;

import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import com.courseshare.subscriptionService.reposetory.SubscriptionReposetory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

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
        boolean isvalidRequest =veriySubscriptionRequest(subscriptionRequest);
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        BeanUtils.copyProperties(subscriptionRequest,subscriptionEntity);
        return subscriptionReposetory.save(subscriptionEntity);
    }

    private boolean veriySubscriptionRequest(SubscriptionRequest subscriptionRequest) {
        RestTemplate restTemplate =
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
