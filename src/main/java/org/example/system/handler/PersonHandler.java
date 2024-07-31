package org.example.system.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.system.dao.PersonDAO;
import org.example.system.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("restriction")
public class PersonHandler implements HttpHandler {

    private final PersonDAO PERSON_DAO = new PersonDAO();
    private final Gson GSON = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().toString();

        try {
            if ("POST".equalsIgnoreCase(method)) {
                handlePostRequest(exchange);
            } else if ("GET".equalsIgnoreCase(method)) {
                if (path.equals("/person")) {
                    handleGetListRequest(exchange);
                } else {
                    handleGetRequest(exchange, path);
                }
            } else if ("PUT".equalsIgnoreCase(method)) {
                handlePutRequest(exchange, path);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                handleDeleteRequest(exchange, path);
            } else {
                sendResponse(exchange, "Método no permitido", 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Error interno del servidor", 500);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Person person = GSON.fromJson(body, Person.class);

        PERSON_DAO.createPerson(person);

        sendResponse(exchange, "Persona creada", 201);
    }

    private void handleGetRequest(HttpExchange exchange, String path) throws IOException {
        int id;
        try {
            id = Integer.parseInt(path.split("/")[2]);
        } catch (NumberFormatException e) {
            sendResponse(exchange, "ID inválido", 400);
            return;
        }

        Person person = PERSON_DAO.getPerson(id);

        if (person != null) {
            String response = GSON.toJson(person);
            sendResponse(exchange, response, 200);
        } else {
            sendResponse(exchange, "Persona no encontrada", 404);
        }
    }

    private void handleGetListRequest(HttpExchange exchange) throws IOException {
        List<Person> personList = PERSON_DAO.getPersonList();

        if (!personList.isEmpty()) {
            String response = GSON.toJson(personList);
            sendResponse(exchange, response, 200);
        } else {
            sendResponse(exchange, "No se encontraron personas", 404);
        }
    }

    private void handlePutRequest(HttpExchange exchange, String path) throws IOException {

        try {

            int id;

            id = Integer.parseInt(path.split("/")[2]);

            String body = readRequestBody(exchange);

            Person updatedPerson = GSON.fromJson(body, Person.class);

            updatedPerson.setId(id);

            if (updatedPerson.getId() == 0) {
                sendResponse(exchange, "ID de persona no proporcionado", 400);
                return;
            }

            boolean updated = PERSON_DAO.updatePerson(updatedPerson);

            if (updated) {
                sendResponse(exchange, "Persona actualizada", 200);
            } else {
                sendResponse(exchange, "Persona no encontrada", 404);
            }

        } catch (NumberFormatException e) {
            sendResponse(exchange, "ID inválido", 400);
        }

    }

    private void handleDeleteRequest(HttpExchange exchange, String path) throws IOException {
        int id;
        try {
            id = Integer.parseInt(path.split("/")[2]);
        } catch (NumberFormatException e) {
            sendResponse(exchange, "ID inválido", 400);
            return;
        }

        PERSON_DAO.deletePerson(id);
        sendResponse(exchange, "Persona eliminada", 200); // 200 OK
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
            os.flush();
        }
    }
}
