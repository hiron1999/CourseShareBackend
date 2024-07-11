package com.example.AwsService.service;

import Model.course.Content;
import Model.course.CourseResourceRequest;

import com.example.AwsService.constaints.AWSResourceConstant;
import com.example.AwsService.entity.DataStore;
import com.example.AwsService.entity.MappingResourceEntity;
import com.example.AwsService.util.AWSutil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
@Slf4j
@Service
public class MsaaageListnerService {
    @Autowired
    private DatabaseManagementService dataService;

    private final ObjectMapper objectMapper;

    public MsaaageListnerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Transactional
    @RabbitListener(queues = "q.content-space-allocator")
    public  void courseResourceMapping(Message message, Channel channel){
        try {
            CourseResourceRequest resourceRequest =objectMapper.readValue(message.getBody(),CourseResourceRequest.class);
            log.info("recived request Content :",resourceRequest);
            // create datastore
            DataStore store =new DataStore();
            store.setStoreId(AWSutil.generateCorseDirectory(resourceRequest.getCourseId()));
            store.setType(AWSResourceConstant.DATA_STORE_TYPE_CONTENT);

            store = dataService.addDataStore(store);
            log.info("created datastore : ",store);

            for(Content content :resourceRequest.getContentList()){
                String resourceType = AWSutil.convertAwsResourceType(content.getContentType());
                MappingResourceEntity mappingResource =dataService.createResourceMapping(content.getId(),store.getStoreId(),resourceType);
                log.info("created resource mapping : ",mappingResource);
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
