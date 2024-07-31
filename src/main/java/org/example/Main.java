package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.system.handler.PersonHandler;

import java.net.InetSocketAddress;
import java.io.IOException;

public class Main {
    @SuppressWarnings("restriction")
    public static void main(String[] args) {
        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            PersonHandler personHandler = new PersonHandler();

            server.createContext("/person", personHandler);

            server.setExecutor(null);

            server.start();

            System.out.println("Server started on port 8080");

        } catch (IOException exception) {
            exception.printStackTrace();
            System.err.println("Failed to start server: " + exception.getMessage());
        }
    }
}
