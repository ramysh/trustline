package com.ripple.service.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The response received for a {@link TransferRequest}
 * @author rpurigella
 */
public class TransferResponse {
    private int code;
    private String message;

    public TransferResponse() {
    }

    public TransferResponse(int code, String message) {
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

    public static TransferResponse okResponse() {
        TransferResponse response = new TransferResponse();
        response.setCode(0);
        response.setMessage("OK");
        return response;
    }

    public static TransferResponse errorResponse(String message) {
        TransferResponse response = new TransferResponse();
        response.setCode(135);
        response.setMessage(message);
        return response;
    }
}

