package com.example.kafkaswing.model;

public class Sample {
    private Transaction transaction;
    private String payer;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public static class Transaction {
        private String id;
        private double amount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}

