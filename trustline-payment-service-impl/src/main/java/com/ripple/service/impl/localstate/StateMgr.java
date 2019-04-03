package com.ripple.service.impl.localstate;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.ripple.service.impl.localstate.StateMgr.ChangeType.CREDIT;
import static com.ripple.service.impl.localstate.StateMgr.ChangeType.DEBIT;

/**
 * Class that manages the {@link State}
 * @author rpurigella
 */
@Component
public class StateMgr {

    private State state;

    public StateMgr() {
        this.state = new State();
    }

    public synchronized void changeState(BigDecimal amount, ChangeType type) throws StateMgrException {
        if (type == CREDIT) {
            this.state.setAmount(this.state.getAmount().subtract(amount));
        } else if (type == DEBIT) {
            this.state.setAmount(this.state.getAmount().add(amount));
        }
    }

    public BigDecimal getBalance() throws StateMgrException {
        return this.state.getAmount();
    }

    public enum ChangeType {
        CREDIT,
        DEBIT
    }
}

