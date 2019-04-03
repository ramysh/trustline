package com.ripple.service.impl;

import com.ripple.service.api.User;
import com.ripple.service.api.UserService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * {@inheritDoc}
 *
 * @author rpurigella
 */
@Component
public class UserServiceImpl implements UserService {

    private Map<String, User> users;

    /**
     * Load hard coded users
     */
    public UserServiceImpl() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.service.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        users = new HashMap<>();

        String userId;
        String userName;
        String userPort;
        String userLocation;
        String userSendPath;
        String userReceivePath;
        String userBalancePath;
        String sendPath;
        String receivePath;
        String balancePath;

        userId = properties.getProperty("user.alice.id");
        userName = properties.getProperty("user.alice.name");
        userPort = properties.getProperty("user.alice.port");
        userLocation = properties.getProperty("user.alice.location");
        userSendPath = properties.getProperty("user.alice.send.path");
        userReceivePath = properties.getProperty("user.alice.receive.path");
        userBalancePath = properties.getProperty("user.alice.balance.path");
        sendPath = userLocation.replace("{port}", userPort) + userSendPath;
        receivePath = userLocation.replace("{port}", userPort) + userReceivePath;
        balancePath = userLocation.replace("{port}", userPort) + userBalancePath;

        users.put(userId, new User(userId, userName, sendPath, receivePath, balancePath));

        userId = properties.getProperty("user.bob.id");
        userName = properties.getProperty("user.bob.name");
        userPort = properties.getProperty("user.bob.port");
        userLocation = properties.getProperty("user.bob.location");
        userSendPath = properties.getProperty("user.bob.send.path");
        userReceivePath = properties.getProperty("user.bob.receive.path");
        userBalancePath = properties.getProperty("user.bob.balance.path");
        sendPath = userLocation.replace("{port}", userPort) + userSendPath;
        receivePath = userLocation.replace("{port}", userPort) + userReceivePath;
        balancePath = userLocation.replace("{port}", userPort) + userBalancePath;

        users.put(userId, new User(userId, userName, sendPath, receivePath, balancePath));
        System.out.println();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
       List<User> userList = new ArrayList<>();
       for (String id : users.keySet()) {
           userList.add(users.get(id));
       }
       return userList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User get(String id) {
        return users.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void register(User user) {
        // Future improvement - servers register with the user service
        throw new UnsupportedOperationException("Method not supported");
    }
}

