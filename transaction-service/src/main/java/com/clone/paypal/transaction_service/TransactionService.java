package com.clone.paypal.transaction_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private RestTemplate restTemplate;
    @Autowired private KafkaProducerService kafkaProducerService;

    private final String walletServiceUrl = "http://WALLET-SERVICE/api/wallets";

    public Transaction performTransaction(Long senderId, Long recipientId, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setSenderId(senderId);
        transaction.setRecipientId(recipientId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        try {
            restTemplate.postForObject(walletServiceUrl + "/debit", new TransactionRequest(senderId, amount), Void.class);
            restTemplate.postForObject(walletServiceUrl + "/credit", new TransactionRequest(recipientId, amount), Void.class);
            transaction.setStatus("COMPLETED");

            String sentMsg = String.format("You sent %.2f to user %d.", amount.doubleValue(), recipientId);
            kafkaProducerService.sendNotificationEvent(new NotificationRequest(senderId, sentMsg));

            String receivedMsg = String.format("You received %.2f from user %d.", amount.doubleValue(), senderId);
            kafkaProducerService.sendNotificationEvent(new NotificationRequest(recipientId, receivedMsg));
        } catch (Exception e) {
            transaction.setStatus("FAILED: " + e.getMessage());
        }
        return transactionRepository.save(transaction);
    }
}