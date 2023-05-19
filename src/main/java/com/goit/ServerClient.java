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

public class ServerClient {
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private static final Request.Builder REQUEST_BUILDER = new Request.Builder();
    private static final String HEADER = "Content-Disposition";
    private static final String FOLDER = "download_files";
    private static final String USER_FOLDER = System.getProperty("user.dir");

    public static void main(String[] args) throws IOException, InterruptedException {
        Request request = REQUEST_BUILDER.get()
                .url("http://localhost:8080/files/072b66c3-e360-492f-98b2-d70abe487d60")
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        Response response = call.execute();
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

    private static Path getPath() {
        return Path.of(USER_FOLDER + File.separator + FOLDER);
    }
}
