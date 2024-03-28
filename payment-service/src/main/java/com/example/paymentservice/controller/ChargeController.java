package com.example.paymentservice.controller;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ChargeController {
    @Autowired
    private StripeService stripeService;

    @PostMapping("/charge")
    public ChargeRequest charge(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        Charge charge = stripeService.charge(chargeRequest);

        Payment payment = new Payment();
        payment.setAmount(charge.getAmount().intValue());
        payment.setCurrency(charge.getCurrency());
        payment.setDescription(charge.getDescription());
        payment.setStatus(charge.getStatus());

        stripeService.savePayment(payment);

        return chargeRequest;
    }

    @GetMapping("/payments")
    public List<Payment> getAllPayments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return stripeService.getAllPayments(pageable);
    }


}