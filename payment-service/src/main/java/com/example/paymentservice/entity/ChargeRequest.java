package com.example.paymentservice.entity;

import lombok.Data;

@Data
public class ChargeRequest {
    private String description;
    private Integer amount;
    private String currency;
    private String stripeEmail;
    private String stripeToken;
    private String customerName;
    private String cardNumber;
    private Integer expMonth;
    private Integer expYear;
    private Integer cvc;
}
