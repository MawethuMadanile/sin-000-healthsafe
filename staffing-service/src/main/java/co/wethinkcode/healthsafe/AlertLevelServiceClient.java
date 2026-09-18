package co.wethinkcode.healthsafe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlertLevelServiceClient {

    private static final String ALERT_LEVEL_URL = "http://localhost:7032/alert-level";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public int fetchAlertLevel() throws IOException, InterruptedException{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ALERT_LEVEL_URL))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() != 200){
            throw new IOException("ward-service returned status " + response.statusCode());
        }
        return mapper.readValue(response.body(), AlertLevel.class).level;
    }
}
