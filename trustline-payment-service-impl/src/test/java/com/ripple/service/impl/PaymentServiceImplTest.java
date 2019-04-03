package com.ripple.service.impl;

import com.ripple.http.TrustlineHttpClient;
import com.ripple.service.api.DebitRequest;
import com.ripple.service.api.DebitResponse;
import com.ripple.service.api.User;
import com.ripple.service.api.exception.PaymentException;
import com.ripple.service.api.service.PaymentService;
import com.ripple.service.impl.localstate.StateMgr;
import com.ripple.service.impl.localstate.StateMgrException;
import com.ripple.service.impl.service.PaymentServiceImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.ripple.service.impl.localstate.StateMgr.ChangeType.CREDIT;
import static com.ripple.service.impl.localstate.StateMgr.ChangeType.DEBIT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author rpurigella
 */
public class PaymentServiceImplTest {

    @Mock
    private TrustlineHttpClient httpClient;

    @Mock
    private StateMgr stateMgr;

    private PaymentService paymentService;

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this);
        paymentService = new PaymentServiceImpl(httpClient, stateMgr);
    }

    @BeforeMethod
    public void resetMocks() {
        reset(httpClient, stateMgr);
    }

    @Test
    public void test_Send_Success() throws IOException {
        BigDecimal value = BigDecimal.TEN;
        when(httpClient.sendDebitRequest(anyString(), any(DebitRequest.class)))
                .thenReturn(new DebitResponse(0, "SUCCESS"));

        paymentService.send(getUsers().get(0), getUsers().get(1), value);

        final ArgumentCaptor<DebitRequest> argumentCaptor = ArgumentCaptor.forClass(DebitRequest.class);
        verify(httpClient, times(1))
                .sendDebitRequest(eq(getUsers().get(1).getReceivePath()), argumentCaptor.capture());
        Assert.assertEquals("A", argumentCaptor.getValue().getFrom().getId());
        Assert.assertEquals(value, argumentCaptor.getValue().getAmount());

        verify(stateMgr, times(1)).changeState(eq(value), eq(CREDIT));
    }

    @Test
    public void test_Send_DebitResponse_Error() throws IOException {
        BigDecimal value = BigDecimal.TEN;
        when(httpClient.sendDebitRequest(anyString(), any(DebitRequest.class)))
                .thenReturn(new DebitResponse(135, "FAILURE"));
        try {
            paymentService.send(getUsers().get(0), getUsers().get(1), value);
        } catch (PaymentException ignored) {}

        final ArgumentCaptor<DebitRequest> argumentCaptor = ArgumentCaptor.forClass(DebitRequest.class);
        verify(httpClient, times(1))
                .sendDebitRequest(eq(getUsers().get(1).getReceivePath()), argumentCaptor.capture());
        Assert.assertEquals("A", argumentCaptor.getValue().getFrom().getId());
        Assert.assertEquals(value, argumentCaptor.getValue().getAmount());

        verify(stateMgr, times(0)).changeState(any(BigDecimal.class), any(StateMgr.ChangeType.class));
    }

    @Test(expectedExceptions = PaymentException.class)
    public void test_Send_StateMgr_Error() throws IOException {
        BigDecimal value = BigDecimal.TEN;
        when(httpClient.sendDebitRequest(anyString(), any(DebitRequest.class)))
                .thenReturn(new DebitResponse(0, "SUCCESS"));
        doThrow(new StateMgrException("Error changing local state"))
                .when(stateMgr).changeState(any(BigDecimal.class), any(StateMgr.ChangeType.class));


        paymentService.send(getUsers().get(0), getUsers().get(1), value);
    }

    @Test(expectedExceptions = PaymentException.class)
    public void test_Send_Http_Error() throws IOException {
        BigDecimal value = BigDecimal.TEN;
        when(httpClient.sendDebitRequest(anyString(), any(DebitRequest.class)))
                .thenThrow(new IOException());
        paymentService.send(getUsers().get(0), getUsers().get(1), value);
    }

    @Test
    public void test_Receive_Success() {
        BigDecimal value = BigDecimal.TEN;
        paymentService.receive(value);
        verify(stateMgr, times(1)).changeState(eq(value), eq(DEBIT));
    }

    @Test(expectedExceptions = PaymentException.class)
    public void test_Receive_StateMgr_Error() {
        doThrow(new StateMgrException("Error changing local state"))
                .when(stateMgr).changeState(any(BigDecimal.class), any(StateMgr.ChangeType.class));
        BigDecimal value = BigDecimal.TEN;
        paymentService.receive(value);
        verify(stateMgr, times(1)).changeState(eq(value), eq(DEBIT));
    }

    @Test
    public void test_Balance() {
        BigDecimal value = BigDecimal.TEN;
        when(stateMgr.getBalance()).thenReturn(value);
        BigDecimal result = paymentService.balance();
        Assert.assertEquals(result, value);
    }


    private List<User> getUsers() {
        return Arrays.asList(
                new User("A", "Alice", "ASendPath", "AReceivePath", "ABalancePath"),
                new User("B", "Bob", "BSendPath", "BReceivePath", "BBalancePath")
        );
    }
}

