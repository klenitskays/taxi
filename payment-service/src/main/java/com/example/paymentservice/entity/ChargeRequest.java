package com.example.paymentservice.entity;

import lombok.Data;

@Data
public class ChargeRequest {
    public enum Currency {
        EUR, USD
    }

    private String description;
    private int amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;
    private String customerName;
    private String cardNumber;
    private int expMonth;
    private int expYear;
    private int cvc;
}
