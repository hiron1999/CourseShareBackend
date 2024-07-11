package com.courseshare.subscriptionService.service;

import Model.SubscriptionModel;
import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import org.springframework.beans.BeanUtils;

public class Convertor {

    public static SubscriptionEntity convertToSubscriotionEntity(SubscriptionModel bean){
       SubscriptionEntity subscriptionEntity =new SubscriptionEntity();
       BeanUtils.copyProperties(bean,subscriptionEntity);
       return subscriptionEntity;
    }
    public static SubscriptionModel convertToSubscriotionModel(SubscriptionEntity entity){
       SubscriptionModel model = new SubscriptionModel();
       BeanUtils.copyProperties(entity,model);
       return model;
    }

}
