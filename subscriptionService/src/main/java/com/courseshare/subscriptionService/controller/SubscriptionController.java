package com.courseshare.subscriptionService.controller;

import java.util.List;

import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import com.courseshare.subscriptionService.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("subscription/{id}")
    public ResponseEntity<?> getSubscription(@PathVariable long id){
       SubscriptionEntity subscription= subscriptionService.getById(id);

      return ResponseEntity.status(HttpStatus.OK).body(subscription);
    }
    @GetMapping("user/{user_id}/subscriptions")
    public List<SubscriptionEntity> getUserSubscriptions(@PathVariable(name ="user_id" ) long userId){
        return subscriptionService.getUserSubscrition(userId);
    }

    @PostMapping("subscription/")
    public SubscriptionEntity addSubscription(@Valid @RequestBody SubscriptionRequest request){
        return subscriptionService.addUserSubscription(request);
    }

}
