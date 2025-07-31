package com.clone.paypal.transaction_service;

import java.math.BigDecimal;

public class TransactionRequest {
    private Long userId;
    private BigDecimal amount;
    public TransactionRequest(Long userId, BigDecimal amount) { this.userId = userId; this.amount = amount; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
