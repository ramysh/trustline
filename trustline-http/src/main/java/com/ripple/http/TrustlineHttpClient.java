package com.ripple.http;

import com.google.gson.Gson;
import com.ripple.service.api.DebitRequest;
import com.ripple.service.api.DebitResponse;
import com.ripple.service.api.TransferRequest;
import com.ripple.service.api.TransferResponse;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Http Client library to used by the server, client and integration tests
 * @author rpurigella
 */
@Component
public class TrustlineHttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    public TrustlineHttpClient() {
        client = new OkHttpClient();
    }

    public TransferResponse sendTransferRequest(String url, TransferRequest transferRequest) throws IOException {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(transferRequest));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return TransferResponse.errorResponse("Error making the http call");
        }
        return TransferResponse.okResponse();
    }

    public DebitResponse sendDebitRequest(String url, DebitRequest debitRequest) throws IOException {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(debitRequest));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            return DebitResponse.errorResponse("Error making the http call");
        }
        return DebitResponse.okResponse();
    }

    public Response getBalance(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        return client.newCall(request).execute();
    }
}

