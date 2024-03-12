package com.example.paymentservice.service;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.repo.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class StripeService {
    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @Autowired
    private PaymentRepository paymentRepository;

    public Charge chargeWithCardDetails(ChargeRequest chargeRequest) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", chargeRequest.getAmount());
        params.put("currency", chargeRequest.getCurrency().toString());
        String description = chargeRequest.getDescription();
        if (description.length() > 499) {
            description = description.substring(0, 499);
        }
        params.put("description", description);

        params.put("customer", createCustomer(chargeRequest));
        params.put("source", createCardObject(chargeRequest));

        return Charge.create(params);
    }

    public Customer createCustomer(ChargeRequest chargeRequest) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", chargeRequest.getStripeEmail());
        customerParams.put("name", chargeRequest.getCustomerName());

        return Customer.create(customerParams);
    }

    private Map<String, Object> createCardObject(ChargeRequest chargeRequest) {
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", chargeRequest.getCardNumber());
        cardParams.put("exp_month", chargeRequest.getExpMonth());
        cardParams.put("exp_year", chargeRequest.getExpYear());
        cardParams.put("cvc", chargeRequest.getCvc());

        return cardParams;
    }
}