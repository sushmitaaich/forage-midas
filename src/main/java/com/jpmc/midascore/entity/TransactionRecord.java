package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many transactions can have same sender
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    // Many transactions can have same recipient
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    protected TransactionRecord() {
        // JPA default constructor
    }

    @Column(nullable = false)
    private float incentive;   // NEW

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) 
    {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    public float getIncentive() {
        return incentive;
    }

    public Long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("TransactionRecord[id=%d, sender=%s, recipient=%s, amount=%f]",
                id,
                sender != null ? sender.getName() : "null",
                recipient != null ? recipient.getName() : "null",
                amount);
    }
}
