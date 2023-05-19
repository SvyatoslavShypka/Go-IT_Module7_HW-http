package com.goit.server.conf;

import com.goit.model.FileModel;
import com.goit.server.exception.ServerException;
import com.goit.server.exception.ServerInternalErrorException;
import com.goit.server.exception.ServerNotFoundException;
import com.goit.server.model.ResponseEntity;
import com.goit.service.FileService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;


@Slf4j
public class SimpleHandler implements HttpHandler {

    private final FileService fileService;

    public SimpleHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        URI endpoint = exchange.getRequestURI();
        log.debug("consumed request:{}, endpoint:{}", requestMethod, endpoint);
        ResponseEntity responseEntity = null;
        try {
            switch (requestMethod) {
                case "GET":
                    responseEntity = handeGET(exchange);
                    break;
                case "POST":
                    responseEntity = handePOST(exchange);
                    break;
                case "PUT":
                    responseEntity = handePUT(exchange);
                    break;
                case "DELETE":
                    responseEntity = handeDELETE(exchange);
                    break;
                default:
                    throw new ServerInternalErrorException("unsupported http method");
            }
        } catch (Exception e) {
            if (e instanceof ServerException) {
                int httpStatus = ((ServerInternalErrorException) e).getHttpCode();
                String responseBody = e.getMessage();
                responseEntity = ResponseEntity.of(responseBody, httpStatus);
            } else {
                responseEntity = ResponseEntity.of(HttpCode.INTERNAL_ERROR);
            }
        } finally {
            sendResponse(exchange, responseEntity, responseEntity.isFile());
        }
    }

    private ResponseEntity handeDELETE(HttpExchange exchange) throws IOException {
        return ResponseEntity.of(HttpCode.NO_CONTENT);
    }

    private ResponseEntity handePUT(HttpExchange exchange) {
        try {
            byte[] bytes = exchange.getRequestBody().readAllBytes();
            log.info(new String(bytes));
        } catch (Exception e) {
            throw new ServerInternalErrorException("can't read request");
        }
        return ResponseEntity.of("updated", HttpCode.CREATED);
    }

    private ResponseEntity handePOST(HttpExchange exchange) {
        String endpoint = exchange.getRequestURI().toString();
        Object data = null;
        if (endpoint.equals("/files")) {
            data = fileService.saveFile(exchange);
        } else if (endpoint.equals("/users")) {
            data = saveUser(exchange);
        } else {
            throw new ServerNotFoundException(endpoint + " not found");
        }

        return ResponseEntity.of(data, HttpCode.CREATED);
    }

    private ResponseEntity handeGET(HttpExchange exchange) {
        String endpoint = exchange.getRequestURI().toString();
        FileModel data = null;
        if (endpoint.startsWith("/files")) {
            // '/files/1'
            String id = endpoint.split("/")[2];
            data = fileService.loadById(id);
        } else if (endpoint.equals("/users")) {
                   data = getUsers(exchange);
        } else {
            throw new ServerNotFoundException(endpoint + " not found");
        }
        return ResponseEntity.of(data, HttpCode.OK, true);
    }

    //TODO
    private FileModel getUsers(HttpExchange exchange) {
        return null;
    }

    //TODO
    private Object saveUser(HttpExchange exchange) {
        try {
            byte[] bytes = exchange.getRequestBody().readAllBytes();
            log.info(new String(bytes));
        } catch (Exception e) {
            throw new ServerInternalErrorException("can't read request");
        }
        return null;
    }

    private static void sendResponse(HttpExchange exchange, ResponseEntity responseEntity) throws IOException {
        exchange.sendResponseHeaders(responseEntity.getStatus(), responseEntity.getDataLength());
        OutputStream os = exchange.getResponseBody();
        os.write(responseEntity.getByteData());
        os.close();
    }

    private static void sendResponse(HttpExchange exchange, ResponseEntity responseEntity, boolean isFile) throws IOException {
        if (isFile) {
            String value = String.format("form-data; name=\"file\"; filename=\"%s\"", responseEntity.getFileName());
            exchange.getResponseHeaders().add("Content-Disposition", value);
        }
        exchange.sendResponseHeaders(responseEntity.getStatus(), responseEntity.getDataLength());
        OutputStream os = exchange.getResponseBody();
        os.write(responseEntity.getByteData());
        os.close();
    }
}
