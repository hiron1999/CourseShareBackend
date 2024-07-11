package com.courseshare.subscriptionService.service;


import java.sql.Date;
import java.time.LocalDate;

import java.util.*;

import Model.SubscriptionModel;
import com.courseshare.subscriptionService.constant.SubscriptionSpecificConstants;
import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import com.courseshare.subscriptionService.reposetory.SubscriptionReposetory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Value("${internal.service.activity.path}")
    private  String ACTIVITY_SERVICE_PATH ;
    @Value("${internal.service.activity.host}")
    private String HOST_IP ;
    @Value("${internal.service.activity.port}")
    private String PORT ;

    @Autowired
    private SubscriptionReposetory subscriptionReposetory;



    private final RestTemplate restTemplate;

    public SubscriptionServiceImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    @Override
    public SubscriptionModel getById(long id) {
        return Convertor.convertToSubscriotionModel(subscriptionReposetory.findById(id).orElseThrow());
    }

    @Override
    public List<SubscriptionModel> getAll() {
        return subscriptionReposetory.findAll()
                .stream()
                .map(Convertor::convertToSubscriotionModel).toList();
    }

    @Override
    public List<SubscriptionModel> getUserSubscrition(long userId) {
        return subscriptionReposetory.findByUserId(userId)
                .stream()
                .filter(sub -> Objects.equals(sub.getStatus(), SubscriptionSpecificConstants.SUBSCRIPTION_STATUS_ACTIVE))
                .map(Convertor::convertToSubscriotionModel).toList();
    }

    @Override
    public SubscriptionModel addUserSubscription(SubscriptionRequest subscriptionRequest) {

        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
        BeanUtils.copyProperties(subscriptionRequest,subscriptionEntity);
        subscriptionEntity.setStatus(SubscriptionSpecificConstants.SUBSCRIPTION_STATUS_ACTIVE);
        subscriptionEntity.setStartDate(Date.valueOf(LocalDate.now()));
        subscriptionEntity.setCourseActivityId(getActivityId(subscriptionRequest.getCourseId()));
        return Convertor.convertToSubscriotionModel( subscriptionReposetory.save(subscriptionEntity));
    }



    @Override
    public SubscriptionModel updateSubscription(SubscriptionModel subscription) {
       if( subscriptionReposetory.findById(subscription.getSubscription_id()).isPresent() ){
          return Convertor.convertToSubscriotionModel(
                  subscriptionReposetory.save(Convertor.convertToSubscriotionEntity(subscription)));

       }else {
           throw new RuntimeException("Subscrition not found!");
       }
    }

    @Override
    public String removeSubscription(long subId) {
        Optional<SubscriptionEntity> subscription= subscriptionReposetory.findById(subId);
        String response = null;
        if(subscription.isPresent()){
            SubscriptionEntity subscriptionEntity = subscription.get();
            subscriptionEntity.setStatus(SubscriptionSpecificConstants.SUBSCRIPTION_STATUS_TERMINATED);
            subscriptionReposetory.save(subscriptionEntity);
            response = "Removed subscription : "  + subId;
        }
        return response;
    }

    private String getActivityId(String courseId){

        String url= HOST_IP + ":" + PORT +ACTIVITY_SERVICE_PATH;
        Map<String ,String>  body = new HashMap<>();
        body.put("courseId" ,courseId );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,String>> request =new HttpEntity<>(body,headers);
        String activityId = null;
        try{
           activityId = restTemplate.postForObject(url,request,String.class);

        }catch (Exception e){

            log.error(e.getLocalizedMessage());
        }
        return activityId ;
    }
}

