package com.goit;

import com.goit.exception.PageNotFoundException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpStatusChecker {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Request.Builder REQUEST_BUILDER = new Request.Builder();
    private static final String HTTP_ANSWER_SITE = "https://http.cat/";

    public static String getStatusImage(int code) {

        Request request = REQUEST_BUILDER.get()
                .url(HTTP_ANSWER_SITE + code)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (response.code() == 404) {
            throw new PageNotFoundException("Page was not found");
        }
        return response.request().url().toString();
    }
}
