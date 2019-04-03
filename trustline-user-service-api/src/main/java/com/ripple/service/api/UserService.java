package com.ripple.service.api;

import java.util.ArrayList;
import java.util.List;

/**
 * For now it is hard coded, but the idea is for it to be run as a remote service.
 * The payment servers on startup will register with the user service with their details.
 *
 * @author rpurigella
 */
public interface UserService {
    /**
     * Gets all the {@link User}'s in the trustline
     * @return An {@link ArrayList} of {@link User}'s
     */
    List<User> getAll();

    /**
     * Returns the {@link User} for a specified {@link User#getId()}
     * @param id The id of the {@link User} to retreive
     * @return {@link User}
     */
    User get(String id);

    /**
     * Currently not supported - For future implementation.
     * Instead of hard coded users, the payment servers on startup will register with the user service with their details.
     * @param user {@link User}
     */
    void register(User user);
}

