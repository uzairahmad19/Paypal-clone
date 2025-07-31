package com.clone.paypal.notification_service;

import com.clone.paypal.transaction_service.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "notification_topic", groupId = "notification_group")
    public void consume(NotificationRequest notificationRequest) {
        logger.info("Consumed Kafka message -> Sending notification to user {}: '{}'",
                notificationRequest.getUserId(), notificationRequest.getMessage());
    }
}