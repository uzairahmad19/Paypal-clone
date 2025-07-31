package com.clone.paypal.transaction_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.performTransaction(
                transaction.getSenderId(), transaction.getRecipientId(), transaction.getAmount()
        );
        return ResponseEntity.ok(newTransaction);
    }
}