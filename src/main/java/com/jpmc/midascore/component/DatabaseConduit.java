package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;   // NEW

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository,
                           RestTemplate restTemplate) 
    {                                                    // NEW param
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    // Existing method used by UserPopulator to save initial users
    public void save(UserRecord userRecord) {
        if (userRecord != null) {
            userRepository.save(userRecord);
        }
    }

    /**
     * Task 3:
     * Validate and apply a transaction:
     *  - sender & recipient IDs must be valid
     *  - sender must have balance >= amount
     * If valid:
     *  - update both balances
     *  - persist a TransactionRecord linked to sender & recipient
     * If invalid:
     *  - do nothing
     */
    @Transactional
    public void processTransaction(Transaction transaction) 
    {
        if (transaction == null) {
            return;
        }

        long senderId = transaction.getSenderId();
        long recipientId = transaction.getRecipientId();
        float amount = transaction.getAmount();

        // 1. Validate sender & recipient exist
        UserRecord sender = userRepository.findById(senderId);
        UserRecord recipient = userRepository.findById(recipientId);

        if (sender == null || recipient == null) {
            // Invalid user IDs -> discard
            return;
        }

        // 2. Validate sender balance
        if (sender.getBalance() < amount) {
            // Insufficient funds -> discard
            return;
        }

        // 3. Call Incentive API
        float incentiveAmount = 0.0f;
        try {
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,           // Spring auto-serializes this Transaction
                    Incentive.class
            );
            if (incentive != null) {
                incentiveAmount = incentive.getAmount();
            }
        } 
        catch (Exception e) {
            // If API fails for some reason, treat incentive as 0 (don’t break normal processing)
            incentiveAmount = 0.0f;
        }

        // 4. Adjust balances
        sender.setBalance(sender.getBalance() - amount);
        // recipient gets both the transfer amount and the incentive
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        userRepository.save(sender);
        userRepository.save(recipient);

        // 5. Record the transaction in the DB, including incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRecordRepository.save(record);
    }

    // Helper method: find a user by id (for debugging / tests)
    public UserRecord findUserById(long id) {
        return userRepository.findById(id);
    }

    // Helper method: list all users (optional, but useful)
    public Iterable<UserRecord> findAllUsers() {
        return userRepository.findAll();
    }
}
