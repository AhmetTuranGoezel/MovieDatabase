package com.frontend.model;


import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Requests {

    private HttpClient client = HttpClient.newHttpClient();
    private HttpRequest request;
    private HttpResponse response;
    private String message;

    private HttpStatus status;
    private String serverURL = "http://localhost:8080";

    public Requests() {
    }

    public HttpResponse post(String uri, String jsonString) throws IOException, InterruptedException {

        request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        setStatus(HttpStatus.valueOf(response.statusCode()));
        setMessage(response.body().toString());

        return response;

    }

    public HttpResponse get(String uri) throws IOException, InterruptedException {

        request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + uri))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        setStatus(HttpStatus.valueOf(response.statusCode()));
        setMessage(response.body().toString());
        return response;

    }

    public HttpResponse put(String uri, String jsonString) throws IOException, InterruptedException {


        request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + uri))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        setStatus(HttpStatus.valueOf(response.statusCode()));
        setMessage(response.body().toString());
        return response;
    }

    public HttpResponse delete(String uri) throws IOException, InterruptedException {

        request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + uri))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        setStatus(HttpStatus.valueOf(response.statusCode()));
        setMessage(response.body().toString());
        return response;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
