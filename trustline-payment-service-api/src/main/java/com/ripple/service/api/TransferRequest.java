package com.ripple.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * The request made by the client to a server
 * @author rpurigella
 */
public class TransferRequest {
    private User from;
    private User to;
    private BigDecimal amount;

    public TransferRequest() {
    }

    public TransferRequest(User from, User to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
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
                .append("to", to)
                .append("amount", amount)
                .toString();
    }
}

