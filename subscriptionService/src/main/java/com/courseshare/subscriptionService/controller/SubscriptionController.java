package com.courseshare.subscriptionService.controller;

import java.util.List;

import Model.SubscriptionModel;
import com.courseshare.subscriptionService.entity.SubscriptionEntity;
import com.courseshare.subscriptionService.model.SubscriptionRequest;
import com.courseshare.subscriptionService.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("subscription/{id}")
    public ResponseEntity<?> getSubscription(@PathVariable long id){
      SubscriptionModel subscription= subscriptionService.getById(id);

      return ResponseEntity.status(HttpStatus.OK).body(subscription);
    }
    @GetMapping("user/{user_id}/subscriptions")
    public List<SubscriptionModel> getUserSubscriptions(@PathVariable(name ="user_id" ) long userId){
        return subscriptionService.getUserSubscrition(userId);
    }

    @PostMapping("subscription/")
    public SubscriptionModel addSubscription(@Valid @RequestBody SubscriptionRequest request){
        return subscriptionService.addUserSubscription(request);
    }
    @DeleteMapping ("subscription/{subId}/remove")
    public ResponseEntity<?> removeSubscription(@PathVariable(name ="subId" ) long subId){
         String result = subscriptionService.removeSubscription(subId);
         ResponseEntity<String> response ;
         if(result !=null){
             response =new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
         }
         else{
             response =ResponseEntity.notFound().build();
         }
         return response;
    }

}
