package com.ripple.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Encapsulates the User in the system
 * @author rpurigella
 */
public class User {
    private String id;
    private String name;
    private String sendPath;
    private String receivePath;
    private String balancePath;

    public User() {
    }

    public User(String id, String name, String sendPath, String receivePath, String balancePath) {
        this.id = id;
        this.name = name;
        this.sendPath = sendPath;
        this.receivePath = receivePath;
        this.balancePath = balancePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendPath() {
        return sendPath;
    }

    public void setSendPath(String sendPath) {
        this.sendPath = sendPath;
    }

    public String getReceivePath() {
        return receivePath;
    }

    public void setReceivePath(String receivePath) {
        this.receivePath = receivePath;
    }

    public String getBalancePath() {
        return balancePath;
    }

    public void setBalancePath(String balancePath) {
        this.balancePath = balancePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("sendPath", sendPath)
                .append("receivePath", receivePath)
                .append("balancePath", balancePath)
                .toString();
    }
}

