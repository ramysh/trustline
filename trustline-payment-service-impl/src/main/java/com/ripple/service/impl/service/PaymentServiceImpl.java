package com.ripple.service.impl.service;

import com.ripple.http.TrustlineHttpClient;
import com.ripple.service.api.DebitRequest;
import com.ripple.service.api.DebitResponse;
import com.ripple.service.api.User;
import com.ripple.service.api.exception.PaymentException;
import com.ripple.service.api.service.PaymentService;
import com.ripple.service.impl.localstate.StateMgr;
import com.ripple.service.impl.localstate.StateMgrException;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.ripple.service.impl.localstate.StateMgr.ChangeType.CREDIT;
import static com.ripple.service.impl.localstate.StateMgr.ChangeType.DEBIT;

/**
 * @author rpurigella
 */
@Component
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private TrustlineHttpClient httpClient;
    private StateMgr stateMgr;

    public PaymentServiceImpl(TrustlineHttpClient httpClient, StateMgr stateMgr) {
        this.httpClient = httpClient;
        this.stateMgr = stateMgr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register() {
        logger.info("{Future Improvement} - registering with the user service..");
    }

    /**
     * {@inheritDoc}
     */
    public void send(User from, User to, BigDecimal amount) throws PaymentException {
        DebitRequest debitRequest = new DebitRequest(from, amount);
        logger.info("Sending payment of {} to {}", debitRequest.getAmount(), to.getName());
        Response response;
        DebitResponse debitResponse;

        try {
            debitResponse = httpClient.sendDebitRequest(to.getReceivePath(), debitRequest);
        } catch (Exception e) {
            logger.error("Error sending payment");
            throw new PaymentException("Error sending payment", e);
        }

        if (debitResponse.getCode() != 0) {
            logger.error("Error sending payment. Debit response = {}", debitResponse);
            throw new PaymentException(debitResponse.getMessage());
        }

        try {
            stateMgr.changeState(amount, CREDIT);
        } catch (StateMgrException e) {
            // possible refund?
            logger.error("Error changing local state on send");
            throw new PaymentException("Error changing local state");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(BigDecimal amount) throws PaymentException {
        try {
            stateMgr.changeState(amount, DEBIT);
        } catch (StateMgrException e) {
            logger.error("Error changing local state on receive");
            throw new PaymentException("Error receiving payment, could not successfully change local state");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal balance() {
        return stateMgr.getBalance();
    }

}

