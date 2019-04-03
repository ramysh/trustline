package com.ripple.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The response received for a {@link DebitRequest}
 * @author rpurigella
 */
public class DebitResponse {
    private int code;
    private String message;

    public DebitResponse() {
    }

    public DebitResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", code)
                .append("message", message)
                .toString();
    }


    public static DebitResponse okResponse() {
        DebitResponse response = new DebitResponse();
        response.setCode(0);
        response.setMessage("OK");
        return response;
    }

    public static DebitResponse errorResponse(String message) {
        DebitResponse response = new DebitResponse();
        response.setCode(135);
        response.setMessage(message);
        return response;
    }
}

