package com.ripple.service.api.service;

import com.ripple.service.api.DebitRequest;
import com.ripple.service.api.User;
import com.ripple.service.api.exception.PaymentException;

import java.math.BigDecimal;

/**
 * @author rpurigella
 */
public interface PaymentService {
    /**
     * For future implementation, servers will register to the user sevice with their details after starting up
     */
    void register();

    /**
     * Sends a {@link DebitRequest} from {@link User} "from" to {@link User} "to".
     * Changes local state to reflect the amount sent.
     * @param from the {@link User} sending the payment
     * @param to the {@link User} receiving the payment
     * @param amount the amount to be sent
     * @throws PaymentException if payment is not successful
     */
    void send(User from, User to, BigDecimal amount) throws PaymentException;

    /**
     * Accepts a payment and changes local state to reflect the amount received.
     * @param amount the amount to receive
     * @throws PaymentException if state change is not successful
     */
    void receive(BigDecimal amount) throws PaymentException;

    /**
     * Gets the balance of the {@link User} from its local state
     * @return {@link BigDecimal} balance
     */
    BigDecimal balance();
}

