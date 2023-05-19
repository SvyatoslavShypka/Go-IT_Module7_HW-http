package com.goit.server.conf;

import com.goit.datasource.Datasource;
import com.goit.repository.FileEntityRepositoryIml;
import com.goit.service.FileService;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Slf4j
public class SimpleHttpServer {
    HttpServer server;

    public SimpleHttpServer(int port, String path, int nThreads) throws IOException {
        InetSocketAddress localhost = new InetSocketAddress("localhost", port);
        HttpServer httpServer = HttpServer.create(localhost, 0);
        Datasource datasource = new Datasource();
        FileEntityRepositoryIml fileRepository = new FileEntityRepositoryIml(datasource);
        FileService fileService = new FileService(fileRepository);
        httpServer.createContext(path, new SimpleHandler(fileService));
        httpServer.setExecutor(Executors.newFixedThreadPool(nThreads));
        server = httpServer;
    }

    public void start() {
        server.start();
        log.info("Started http server, listening port:{}", server.getAddress().getPort());
    }
}
