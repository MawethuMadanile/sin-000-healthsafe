package co.wethinkcode.healthsafe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class WardServiceClient {
    private static final String WARD_SERVICE_BASE = "http://localhost:7031";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<Ward> fetchWard(String wardId) throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(WARD_SERVICE_BASE + "/wards/" + wardId))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404){
            return Optional.empty();
        }
        if (response.statusCode() != 200){
            throw new IOException("ward-service returned status " + response.statusCode());
        }
        return Optional.of(mapper.readValue(response.body(), Ward.class));
    }
}
