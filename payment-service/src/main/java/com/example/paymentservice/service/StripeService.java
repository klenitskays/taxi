package com.example.paymentservice.service;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repo.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StripeService {
    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @Autowired
    private PaymentRepository paymentRepository;

    public Charge charge(ChargeRequest chargeRequest) throws StripeException {
        Stripe.apiKey = secretKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", chargeRequest.getAmount());
        params.put("currency", "usd");
        params.put("description", "Default description");
        params.put("source", "tok_visa");

        return Charge.create(params);
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).getContent();
    }

}