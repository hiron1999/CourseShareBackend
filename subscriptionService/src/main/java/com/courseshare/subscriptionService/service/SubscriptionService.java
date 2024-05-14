package com.courseshare.subscriptionService.service;

import java.util.List;

import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;

public interface SubscriptionService {

    public SubscriptionEntity getById(long id);
    public List<SubscriptionEntity> getAll();
    public List<SubscriptionEntity> getUserSubscrition(long userId);

    public SubscriptionEntity addUserSubscription(SubscriptionRequest request);

    public SubscriptionEntity updateSubscription(SubscriptionEntity subscription);

}
