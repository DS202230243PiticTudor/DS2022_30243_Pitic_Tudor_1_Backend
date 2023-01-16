package com.ds.management.web.consumer;

import com.ds.management.domain.dtos.QueueMessage;
import com.ds.management.domain.entities.Notification;
import com.ds.management.services.QueueMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ds.management.config.RabbitMQConfig.QUEUE_NAME;

@Component
@Slf4j
public class Consumer {
    private QueueMessageService queueMessageService;

    @Autowired
    public Consumer(QueueMessageService queueMessageService) {
        this.queueMessageService = queueMessageService;
    }
    @RabbitListener(queues = QUEUE_NAME)
    private void receive(Object[] messages) {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(messages);
        List<QueueMessage> messageList = Arrays.stream(messages)
                .map(message -> mapper.convertValue(message, QueueMessage.class))
                .collect(Collectors.toList());
        log.info("Message received from appQueue -> {}", messageList);
        this.queueMessageService.processQueueMessages(messageList);
    }
}
