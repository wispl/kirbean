package me.wisp.kirbean.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HTTPClient {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode getWithQuery(String uri, String query) {
        return parseAsJson(sendRequest(URI.create(uri + encodeQuery(query))));

    }
    public static JsonNode get(URI uri) {
        return parseAsJson(sendRequest(uri));
    }

    private static JsonNode parseAsJson(HttpResponse<String> response) {
        try {
            return mapper.readTree(response.body());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpResponse<String> sendRequest(URI uri) {
        try {
            return client.send(buildRequest(uri), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest buildRequest(URI uri) {
        return HttpRequest.newBuilder(uri).build();
    }

    private static String encodeQuery(String query) {
        return URLEncoder.encode(query, StandardCharsets.UTF_8);
    }
}

