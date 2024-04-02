package com.example.paymentservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ChargeRequest {
    private String description;
    private Integer amount;
    private String currency;
    private String stripeToken;

}