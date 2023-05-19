package com.goit;

import com.goit.exception.PageNotFoundException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpStatusImageDownloader {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Request.Builder REQUEST_BUILDER = new Request.Builder();
    private static final String HEADER = "Content-Type";
    private static final String USER_FOLDER = System.getProperty("user.dir");

    public static void downloadStatusImage(int code) {
        HttpStatusChecker httpStatusChecker = new HttpStatusChecker();
//        String link = httpStatusChecker.getStatusImage(code);
        String link = HttpStatusChecker.getStatusImage(code);

        Request request = REQUEST_BUILDER.get()
                .url(link)
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
        String extension = response.header(HEADER).split("/")[1];
        String fileName = code + "." + extension;
        InputStream inputStream = response.body().byteStream();
        Path path = Path.of(USER_FOLDER);
        try {
        Files.createDirectories(path);
        File file = new File(path + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(inputStream.readAllBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
