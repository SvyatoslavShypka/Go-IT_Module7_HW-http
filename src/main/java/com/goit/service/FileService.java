package com.goit.service;

import com.goit.entity.FileEntity;
import com.goit.model.FileModel;
import com.goit.repository.JDBCRepository;
import com.goit.server.exception.ServerInternalErrorException;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class FileService {
    private static final String FILE_NAME_HEADER = "file_name";
    private static final String FILE_STORAGE_FOLDER = "files";
    private static final String USER_FOLDER = System.getProperty("user.dir");

    private final JDBCRepository<FileEntity> fileRepository;

    public FileService(JDBCRepository<FileEntity> fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFile(HttpExchange exchange) {
        try {
            String fileName = exchange.getRequestHeaders().get(FILE_NAME_HEADER).get(0);
            String[] fileNameParts = fileName.split("\\.");
            String name = fileNameParts[0];
            String extension = fileNameParts[1];
            byte[] bytes = exchange.getRequestBody().readAllBytes();
            FileEntity entity = FileEntity.of(null, name, extension, Instant.now().toEpochMilli());
            FileEntity saved = fileRepository.save(entity);
            Path path = getPath();
            Files.createDirectories(path);
            File file = new File(path + File.separator + saved.getId() + "." + extension);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return saved.getId();
        } catch (Exception e) {
            throw new ServerInternalErrorException("save file");
        }
    }

    private static Path getPath() {
        return Path.of(USER_FOLDER + File.separator + FILE_STORAGE_FOLDER);
    }

    public FileModel loadById(String id) {
        try {
            FileEntity byId = fileRepository.findById(id);
            Path path = getPath();
            Files.createDirectories(path);
            File file = new File(path + File.separator + byId.getId() + "." + byId.getExtension());
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();
            return FileModel.of(byId.getId(), byId.getName(), byId.getExtension(), byId.getCreationDate(), bytes);
        } catch (Exception e) {
            throw new ServerInternalErrorException("save file");
        }
    }
}
