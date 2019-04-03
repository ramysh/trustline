package com.ripple.service.impl.localstate;

import com.ripple.service.api.User;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Object holding the state of the {@link User}
 * @author rpurigella
 */
public class State {
    private volatile BigDecimal amount;
    private String currency;

    public State() {
        this.amount = BigDecimal.ZERO;
        this.currency = "USD";
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("amount", amount)
                .append("currency", currency)
                .toString();
    }
}

