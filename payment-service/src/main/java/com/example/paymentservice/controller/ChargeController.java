package com.example.paymentservice.controller;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repo.PaymentRepository;
import com.example.paymentservice.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargeController {
    @Autowired
    private StripeService stripeService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/charge")
    public String charge(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        Charge charge = stripeService.chargeWithCardDetails(chargeRequest);

        Payment payment = new Payment();
        payment.setAmount(chargeRequest.getAmount());
        payment.setCurrency(chargeRequest.getCurrency().toString());
        payment.setDescription(chargeRequest.getDescription());
        payment.setStatus(charge.getStatus());

        paymentRepository.save(payment);

        return payment.getId().toString();
    }
}