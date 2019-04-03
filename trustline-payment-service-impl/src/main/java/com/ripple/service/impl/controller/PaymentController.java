package com.ripple.service.impl.controller;

import com.ripple.service.api.*;
import com.ripple.service.api.exception.PaymentException;
import com.ripple.service.api.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Controller that accepts the http requests
 * @author rpurigella
 */
@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostConstruct
    public void init() {
        /* Future improvement - servers register to the user service */
        // paymentService.register();
        logger.info("Welcome to Trustline!");
        logger.info("Trustline balance is {}", paymentService.balance());
    }

    /**
     * Gets the balance of the {@link User}
     * @return balance
     */
    @GetMapping(path = "/balance")
    public String getBalance() {
        return paymentService.balance().toString();
    }

    /**
     * Accepts a {@link TransferRequest} to send {@link TransferRequest#getAmount()} from {@link TransferRequest#getFrom()}
     * to {@link TransferRequest#getTo()}
     * Sends the funds and subtracts from the local state
     *
     * @param transferRequest The {@link TransferRequest} to process
     * @return {@link TransferResponse}
     */
    @PostMapping(path = "/transfer")
    @ResponseBody
    public TransferResponse sendPayment(@RequestBody TransferRequest transferRequest) {
        logger.debug("Sending {}", transferRequest);
        try {
            paymentService.send(transferRequest.getFrom(), transferRequest.getTo(), transferRequest.getAmount());
        } catch (PaymentException e) {
            return TransferResponse.errorResponse(e.toString());
        }
        logger.info("Sent");
        logger.info("Trustline balance is {}", paymentService.balance());
        return TransferResponse.okResponse();
    }

    /**
     * Accepts a {@link DebitRequest} of amount {@link DebitRequest#getAmount()} from {@link DebitRequest#getFrom()}
     * Adds the funds to the local state
     * @param debitRequest {@link DebitRequest}
     * @return {@link DebitResponse}
     */
    @PostMapping(path = "/debit")
    @ResponseBody
    public DebitResponse receivePayment(@RequestBody DebitRequest debitRequest) {
        logger.debug("Receiving {}", debitRequest);
        try {
            paymentService.receive(debitRequest.getAmount());
        } catch (PaymentException e) {
            return DebitResponse.errorResponse(e.toString());
        }
        logger.info("You were paid {}!", debitRequest.getAmount());
        logger.info("Trustline balance is : {}", paymentService.balance());
        return DebitResponse.okResponse();
    }


}

