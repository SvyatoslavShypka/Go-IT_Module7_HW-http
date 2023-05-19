package com.goit.server.model;

import com.goit.model.FileModel;
import com.goit.server.conf.HttpCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class ResponseEntity {

    private final byte[] byteData;
    private final Integer status;
    private final String statusMessage;
    private String fileName;
    private boolean file;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public ResponseEntity(Object data, Integer status) {
        if (data instanceof String) {
            this.byteData = ((String) data).getBytes(StandardCharsets.UTF_8);
        } else {
            this.byteData = gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        }
        this.status = status;
        this.statusMessage = HttpCode.msg(status);
    }

    public ResponseEntity(FileModel data, Integer status, boolean file) {
        this.byteData = data.getData();
        this.status = status;
        this.statusMessage = HttpCode.msg(status);
        this.fileName = data.getName() + "." + data.getExtension();
        this.file = file;
    }

    public ResponseEntity(byte[] data, Integer status) {
        this.byteData = data;
        this.status = status;
        this.statusMessage = HttpCode.msg(status);
    }

    public static ResponseEntity of(Object data, Integer status) {
        return new ResponseEntity(data, status);
    }

    public static ResponseEntity of(FileModel data, Integer status, boolean file) {
        return new ResponseEntity(data, status, file);
    }

    public static ResponseEntity of(byte[] data, Integer status) {
        return new ResponseEntity(data, status);
    }

    public static ResponseEntity of(Integer status) {
        return new ResponseEntity(new byte[0], status);
    }

    public int getDataLength() {
        return byteData.length;
    }
}
