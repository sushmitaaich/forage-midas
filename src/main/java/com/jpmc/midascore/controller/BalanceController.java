package com.jpmc.midascore.controller;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    private final DatabaseConduit databaseConduit;

    public BalanceController(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    /**
     * GET /balance?userId=123
     *
     * - If user exists: return Balance with their current balance.
     * - If user does not exist: return Balance with amount 0.
     */
    @GetMapping("/balance")
    public Balance getBalance(@RequestParam("userId") long userId) {
        UserRecord user = databaseConduit.findUserById(userId);

        float amount = 0.0f;
        if (user != null) {
            amount = user.getBalance();
        }

        // Use the existing Balance class as-is. Do NOT change its toString().
        return new Balance(userId, amount);
        // ^ if your Balance constructor is different, use the one defined in Balance.java
    }
}
