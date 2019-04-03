package com.ripple.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * The request used for sending a payment from a server
 * @author rpurigella
 */
public class DebitRequest {
    private User from;
    private BigDecimal amount;

    public DebitRequest() {
    }

    public DebitRequest(User from, BigDecimal amount) {
        this.from = from;
        this.amount = amount;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("amount", amount)
                .toString();
    }
}

