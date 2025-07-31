package com.clone.paypal.wallet_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletRepository walletRepository;

    // A simple endpoint to get a wallet by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Wallet> getWalletByUserId(@PathVariable Long userId) {
        return walletRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // We'll also need a way to create wallets.
    // In a real system, the User Service would call this automatically after a user registers.
    // For now, we can add a manual endpoint for testing.
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
        wallet.setBalance(BigDecimal.ZERO); // Wallets start with a zero balance
        wallet.setCurrency("INR");
        Wallet savedWallet = walletRepository.save(wallet);
        return ResponseEntity.ok(savedWallet);
    }
    // Add these new methods to your existing WalletController

    @PostMapping("/debit")
    public ResponseEntity<Void> debit(@RequestBody TransactionRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId()).orElse(null);
        if (wallet == null || wallet.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().build(); // Wallet not found or insufficient funds
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credit")
    public ResponseEntity<Void> credit(@RequestBody TransactionRequest request) {
        Wallet wallet = walletRepository.findByUserId(request.getUserId()).orElse(null);
        if (wallet == null) {
            return ResponseEntity.badRequest().build(); // Wallet not found
        }
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        return ResponseEntity.ok().build();
    }

}
