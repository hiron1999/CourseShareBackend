package com.courseshare.contentService.service;


import java.net.URI;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Model.course.Content;
import Model.course.ContentType;
import Model.course.CourseResourceRequest;
import Model.SubscriptionModel;
import com.courseshare.contentService.entity.CourseEntity;
import com.courseshare.contentService.entity.Module;
import com.courseshare.contentService.model.CourseDisplayModel;
import com.courseshare.contentService.model.UserContentDetails;
import com.courseshare.contentService.reposetory.CourseReposetory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static java.util.stream.Collectors.toList;

@Service
public class ContentServiceImpl implements ContentService{

    public static final String COURSE_SUBSCRIPTION_ACTIVE = "Active";
    @Value("${internal.service.subscription.path}")
    private String subscriptionServicePath;
    @Value("${internal.service.subscription.domain}")
    private String subscriptionDomain;

    @Value("${spring.rabbitmq.exchange.aws-store}")
    private String awsStoreExchange;

    @Value("${spring.rabbitmq.route-key.space-allocate}")
    private String spaceAllocateRouteKey;

    @Autowired
    private CourseReposetory reposetory;
    @Autowired
//    private ModuleReposetory moduleReposetory;

    private final MongoTemplate mongoTemplate;

    private final RestTemplate restTemplate;

    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @Autowired
    private  ContentActivityService activityService;

    @Autowired
    public ContentServiceImpl(MongoTemplate mongoTemplate, RestTemplateBuilder templateBuilder){
        this.mongoTemplate=mongoTemplate;
        restTemplate = templateBuilder.build();
    }

    @Override
    public List<CourseDisplayModel> getALL() {
//        return reposetory.findAll();
        Query query = new Query();
        query.fields().exclude("modules");
        List<CourseEntity> courseEntities= mongoTemplate.find(query,CourseEntity.class);


        return courseEntities.stream().map(course ->{
            CourseDisplayModel model =new CourseDisplayModel();
             BeanUtils.copyProperties(course,model);
             return model;
        }).collect(toList());
    }

    @Transactional
    @Override
    public CourseEntity add(CourseEntity course) {

        CourseEntity savedCource = reposetory.save(course);
        requrstResourceMapping(savedCource);
        return savedCource;
    }

    @Override
    public UserContentDetails getDetail(String courseId,long userId) {
        UserContentDetails contentDetails = new UserContentDetails();
        Optional<SubscriptionModel> subscription = getSubscriptionDetails(userId,courseId);
        CourseEntity courseEntity = reposetory.findById(courseId).orElseThrow();

        if(subscription.isPresent() && Objects.equals(subscription.get().getStatus(), COURSE_SUBSCRIPTION_ACTIVE)){
            contentDetails.setSubscriptionStatus(COURSE_SUBSCRIPTION_ACTIVE);
            contentDetails.setSubscriptionID(subscription.get().getSubscription_id());
            contentDetails.setCourseEntity(courseEntity);
            contentDetails.setActivityStatusId(subscription.get().getCourseActivityId());
        }else{

            List<Module> limitedModuleList = courseEntity.getModules().stream().limit(1).toList();

            courseEntity.setModules(limitedModuleList);

//            contentDetails.setSubscriptionStatus(COURSE_SUBSCRIPTION_ACTIVE);
//            contentDetails.setSubscriptionID(subscription.getSubscription_id());
            contentDetails.setCourseEntity(courseEntity);
//            contentDetails.setActivityStatus(activityService.get(subscription.getCourseActivityId()));
        }

        return contentDetails;
    }




    private Optional<SubscriptionModel> getSubscriptionDetails(long userId, String courseId) {

        List<SubscriptionModel> subscriptions = getSubscriptionList(userId);
        return subscriptions.stream().filter(sub -> Objects.equals(sub.getCourseId(), courseId)).findAny();
    }

    private List<SubscriptionModel> getSubscriptionList(long userId) {

        List<SubscriptionModel> subscriptionList =null;
        String generatedpath = subscriptionServicePath.replace("/{user_id}/",String.format("/%d/",userId));
        try {
            URI url = new URI( subscriptionDomain + generatedpath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<List<SubscriptionModel>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SubscriptionModel>>() {}
            );
            subscriptionList =responseEntity.getBody();

        } catch (Exception  e) {
            throw  new RuntimeException(e);
        }

       return  subscriptionList;
    }

    private void requrstResourceMapping(CourseEntity course){

        CourseResourceRequest request = generateResourceRequest(course);

        rabbitTemplate.convertAndSend(awsStoreExchange,spaceAllocateRouteKey,request);
    }

    private CourseResourceRequest generateResourceRequest(CourseEntity course) {
        CourseResourceRequest resourceRequest = new CourseResourceRequest();
        resourceRequest .setCourseId(course.getId());


        List<Content> contentList = course.getModules().stream()
                                    .flatMap(module -> module.getLectures().stream())
                                    .map(leacture -> {
                                        Content content = new Content();
                                        content.setId(leacture.getId());
                                        content.setContentType(ContentType.VIDEO);

                                        return content;
                                    } ).toList();

        resourceRequest.setContentList(contentList);
        return resourceRequest;
    }


}
