package com.ripple.client;

import com.ripple.http.TrustlineHttpClient;
import com.ripple.service.api.TransferRequest;
import com.ripple.service.api.TransferResponse;
import com.ripple.service.api.User;
import com.ripple.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;


/**
 * Console App for testing the Trustline
 * @author rpurigella
 */
@Component
public class ClientApp {

    private TrustlineHttpClient trustlineHttpClient;
    private UserService userService;
    private Scanner in;

    @Autowired
    public ClientApp(TrustlineHttpClient trustlineHttpClient, UserService userService) {
        this.trustlineHttpClient = trustlineHttpClient;
        this.userService = userService;
        InputStream source = System.in;
        this.in = new Scanner(source);
    }

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext("com.ripple.client", "com.ripple.http", "com.ripple.service.impl");
        ClientApp clientApp = ctx.getBean(ClientApp.class);
        clientApp.start();
    }

    private void start() throws IOException {
        System.out.println("This is a console app to test the Trustline");
        System.out.println();
        System.out.println("These are the user id's in the system");
        List<User> users = userService.getAll();
        System.out.println(users.get(0).getId());
        System.out.println(users.get(1).getId());
        System.out.println();

        String option;
        while (true) {
            option = getInput();
            String fromId;
            String toId;
            BigDecimal amount;
            try {
                String[] options = option.split(" ");
                fromId = options[0];
                toId = options[1];
                amount = new BigDecimal(options[2]);
            } catch (Exception e) {
                System.out.println("Invalid input. Enter input again. Error : " + e.toString());
                continue;
            }
            User from = userService.get(fromId);
            if (from == null) {
                System.out.println("User " + fromId + " does not exist");
                continue;
            }
            User to = userService.get(toId);
            if (to == null) {
                System.out.println("User " + toId + " does not exist");
                continue;
            }
            TransferRequest transferRequest = new TransferRequest(from, to, amount);
            TransferResponse transferResponse;
            try {
                 transferResponse = trustlineHttpClient.sendTransferRequest(from.getSendPath(), transferRequest);
            } catch (Exception e) {
                System.out.println("Error sending payment " + e.toString());
                continue;
            }

            if (transferResponse.getCode() == 0) {
                System.out.println("Payment request sent");
            } else {
                System.out.println("Error sending payment : " + transferResponse.getMessage());
            }
        }
    }

    private String getInput() {
        System.out.println();
        System.out.println("Enter input in the following format {fromId} {toId} {amount}");
        System.out.println("Example: A B 45");
        System.out.println("Control + C to quit");
        return in.nextLine().toUpperCase();
    }


}

