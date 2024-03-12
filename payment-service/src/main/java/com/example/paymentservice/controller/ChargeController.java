package com.example.paymentservice.controller;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ChargeController {
    @Autowired
    private StripeService stripeService;

    @PostMapping("/charge")
    public String charge(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        Charge charge = stripeService.charge(chargeRequest);

        Payment payment = new Payment();
        payment.setAmount(charge.getAmount().intValue());
        payment.setCurrency(charge.getCurrency());
        payment.setDescription(charge.getDescription());
        payment.setStatus(charge.getStatus());

        stripeService.savePayment(payment);

        return payment.getId().toString();
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments() {
        return stripeService.getAllPayments();
    }


}