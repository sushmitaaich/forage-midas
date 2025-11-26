package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction 
{
    private String rawLine;
    private long senderId;
    private long recipientId;
    private float amount;

    public Transaction() {
        // Default constructor
    }

    public Transaction(String transactionLine) {
        this.rawLine = transactionLine;
    }

    public Transaction(long senderId, long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(long recipientId) {
        this.recipientId = recipientId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction {senderId=" + senderId + ", recipientId=" + recipientId + ", amount=" + amount + "}";
    }

    public static Transaction fromString(String transactionLine) {
    if (transactionLine == null || transactionLine.isBlank()) {
        throw new IllegalArgumentException("transactionLine cannot be null or empty");
    }

    String[] parts = transactionLine.split(",\\s*");
    if (parts.length != 3) {
        throw new IllegalArgumentException("Invalid transaction format: " + transactionLine);
    }

    long senderId = Long.parseLong(parts[0]);
    long recipientId = Long.parseLong(parts[1]);
    float amount = Float.parseFloat(parts[2]);

    Transaction tx = new Transaction(senderId, recipientId, amount);
    tx.setRawLine(transactionLine);   // if you want to keep the original line
    return tx;
    }
    public String getRawLine() {
        return rawLine;
    }
    public void setRawLine(String rawLine) {
        this.rawLine = rawLine;
    }
}
