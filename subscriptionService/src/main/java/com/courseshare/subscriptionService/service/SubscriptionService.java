package com.courseshare.subscriptionService.service;

import java.util.List;


import Model.SubscriptionModel;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import org.springframework.stereotype.Service;


public interface SubscriptionService {

    public SubscriptionModel getById(long id);
    public List<SubscriptionModel> getAll();
    public List<SubscriptionModel> getUserSubscrition(long userId);

    public SubscriptionModel addUserSubscription(SubscriptionRequest request);

    public SubscriptionModel updateSubscription(SubscriptionModel subscription);
    public String removeSubscription(long subId);
}
