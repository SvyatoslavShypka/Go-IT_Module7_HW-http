package com.goit;

import com.goit.conf.FlywayConfigurations;
import com.goit.conf.LoggingConfiguration;
import com.goit.server.conf.SimpleHttpServer;

import java.io.IOException;

public class ServerApplication {
    public static void main(String[] args) throws IOException {
        new LoggingConfiguration();
        new FlywayConfigurations().setup().migrate();
        final int port = 8080;
        final String path = "/";
        final int nThreads = 4;
        SimpleHttpServer server = new SimpleHttpServer(port, path, nThreads);
        server.start();
    }
}
