package com.goit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpStatusChecker {

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Request.Builder REQUEST_BUILDER = new Request.Builder();
    private static final String HEADER = "Content-Disposition";
    private static final String FOLDER = "download_files";
    private static final String USER_FOLDER = System.getProperty("user.dir");
    private static final String HTTP_ANSWER_SITE = "https://http.cat/";

    public static void main(String[] args) {
        HttpStatusChecker httpStatusChecker = new HttpStatusChecker();
        String link = httpStatusChecker.getStatusImage(300);
        System.out.println("link = " + link);

        Request request = REQUEST_BUILDER.get()
                .url("https://http.cat/300")
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String fileNamePart = response.header(HEADER).split(";")[2];
        String fileName = fileNamePart.split("\"")[1];
        InputStream inputStream = response.body().byteStream();

        Path path = getPath();
        Files.createDirectories(path);
        File file = new File(path + File.separator + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(inputStream.readAllBytes());
        fileOutputStream.flush();
        fileOutputStream.close();


    }

    String getStatusImage(int code) {
        String result = HTTP_ANSWER_SITE + code;
        return result;
    }

    private static Path getPath() {
        return Path.of(USER_FOLDER + File.separator + FOLDER);
    }
}
