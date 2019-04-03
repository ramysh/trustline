package com.ripple.service;

import com.ripple.http.TrustlineHttpClient;
import com.ripple.service.api.TransferRequest;
import com.ripple.service.api.User;
import com.ripple.service.api.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Sends a series of 300 {@link TransferRequest}'s over 30 threads from Server A to B
 * Sends the same requests over 30 threads from Server B to A, but doesnt send the last one.
 *
 * The final balance at each server is verified to correctly reflect the {@link TransferRequest}'s sent to both servers
 * @author rpurigella
 */
@Test
public class PaymentServiceIT {

    private static final int numThreads = 30;
    private static final int numRequests = 300;
    private static final int amountMaxRange = 1000;

    private UserService userService;
    private TrustlineHttpClient httpClient;

    @BeforeClass
    public void init() {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("com.ripple.service.impl", "com.ripple.http");

        this.userService = ctx.getBean(UserService.class);
        this.httpClient = ctx.getBean(TrustlineHttpClient.class);
    }

    @Test
    public void test_Concurrent_Payments() throws IOException {
        Random random = new Random();
        BigDecimal[] amounts = new BigDecimal[numRequests];
        for (int i = 0; i < amounts.length; i++) {
            amounts[i] = new BigDecimal(random.nextInt(amountMaxRange));
        }
        List<User> users = userService.getAll();
        User from = users.get(0);
        User to = users.get(1);

        // get the current balance. it might be nom zero
        BigDecimal beforeBalanceA = new BigDecimal(Objects.requireNonNull(httpClient.getBalance(from.getBalancePath()).body()).string());
        BigDecimal beforeBalanceB = new BigDecimal(Objects.requireNonNull(httpClient.getBalance(to.getBalancePath()).body()).string());

        // Send all 300 requests from A to B
        Thread thread1 = new Thread(new Processor(amounts, amounts.length, from, to));
        //Send only 299 from B to A
        Thread thread2 = new Thread(new Processor(amounts, amounts.length - 1, to, from));
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException ignored) {}

        // get balances after the requests are processed
        BigDecimal afterBalanceA = new BigDecimal(Objects.requireNonNull(httpClient.getBalance(from.getBalancePath()).body()).string());
        BigDecimal afterBalanceB = new BigDecimal(Objects.requireNonNull(httpClient.getBalance(to.getBalancePath()).body()).string());

        // make sure the balances are correctly updated
        Assert.assertEquals(amounts[amounts.length - 1].add(beforeBalanceB), afterBalanceB);
        Assert.assertEquals(beforeBalanceA.subtract(amounts[amounts.length - 1]), afterBalanceA);
    }

    private class Processor implements Runnable {
        private BigDecimal[] amounts;
        private int lengthToProcess;
        private User from;
        private User to;

        public Processor(BigDecimal[] amounts, int lengthToProcess, User from, User to) {
            this.amounts = amounts;
            this.lengthToProcess = lengthToProcess;
            this.from = from;
            this.to = to;
        }

        @Override
        public void run() {
            ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

            for (int i = 0; i < lengthToProcess; i++) {
                TransferRequest transferRequest = new TransferRequest(from, to, amounts[i]);
                executorService.submit(new Caller(from.getSendPath(), transferRequest));
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException ignored) {}
        }
    }

    private class Caller implements Runnable {

        private String url;
        private TransferRequest request;

        public Caller(String url, TransferRequest request) {
            this.url = url;
            this.request = request;
        }

        @Override
        public void run() {
            try {
                httpClient.sendTransferRequest(url, request);
            }catch (IOException ignored) {}
        }
    }

}

