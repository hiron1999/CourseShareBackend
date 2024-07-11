package com.courseshare.contentService.configration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.queue.initiate-content}")
    private String initiateContentQueue;

    @Value("${spring.rabbitmq.exchange.aws-store}")
    private String awsStoreExchange;

    @Value("${spring.rabbitmq.route-key.space-allocate}")
    private String spaceAllocateRouteKey;

    @Value("${spring.rabbitmq.exchange.dlx}")
    private  String dlxExchangeMessages ;
    @Value("${spring.rabbitmq.queue.dlq}")
    private  String dlxQueue ;


    @Bean
    public Queue createContentSpaceallocationQueue() {
        return  QueueBuilder.durable(initiateContentQueue)
                .withArgument("x-dead-letter-exchange", dlxExchangeMessages)
                .build();
    }
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlxQueue).build();
    }
    @Bean
    public TopicExchange awsStorageExchange(){
        return new TopicExchange(awsStoreExchange);
    }
    @Bean
    public FanoutExchange dlxexchange(){
        return new FanoutExchange(dlxExchangeMessages);
    }

    @Bean
    public Binding massageQbinding(@Qualifier("createContentSpaceallocationQueue") Queue queue , TopicExchange exchange){
       return BindingBuilder .bind(queue).to(exchange).with(spaceAllocateRouteKey);
    }
    @Bean
    public Binding dlxbinding(){
        return BindingBuilder.bind(deadLetterQueue()).to(dlxexchange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
