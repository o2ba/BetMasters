package service.general.external.apiService;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import okhttp3.*;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * ApiRequestImpl is an implementation of the ApiRequest interface.
 * It provides functionality to send requests to an API using OkHttpClient.
 */
@Component
public class ApiRequestImpl implements ApiRequest {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestImpl.class);

    private static final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://v3.football.api-sports.io/";
    private static final String HOST = "v3.football.java.backend.model.api-sports.io";

    @NotNull
    private static final String KEY;

    @Nullable
    private Headers responseHeaders;
    private int responseCode = 0;
    @Nullable
    private String errors;

    static {
        try {
            KEY = getToken();
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Failed to initialize API key: " + e.getMessage());
        }
    }

    private static String getToken() throws IllegalStateException {
        String token = System.getenv("API_KEY");
        if (token != null) {
            logger.info("Token successfully read from environment variable");
        } else {
            logger.error("API_KEY environment variable not found.");
            throw new IllegalStateException("API_KEY environment variable not found");
        }

        if (!isTokenValid(token)) {
            logger.error("Token is invalid. Please check the API key.");
            throw new IllegalStateException("Token is invalid. Please check the API key.");
        } else {
            logger.info("Token is validated successfully");
        }

        return token;
    }

    private static boolean isTokenValid(String token) {
        Request request = new Request.Builder()
                .url(API_URL + "status")
                .get()
                .addHeader("x-apisports-key", token)
                .addHeader("x-apisports-host", HOST)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.debug("Response was not successful. Response code: {}", response.code());
                return false;
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                logger.debug("Response body was null");
                return false;
            }

            JsonElement jsonElement = new Gson().fromJson(responseBody.string(), JsonElement.class);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("errors") && jsonObject.get("errors").isJsonObject()) {
                JsonObject errorObject = jsonObject.get("errors").getAsJsonObject();
                if (errorObject.has("token")) {
                    return errorObject.get("token").getAsString().isEmpty();
                }
            }

            return true;

        } catch (IOException e) {
            logger.error("Failed to call API to validate token: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public JsonElement sendRequest(@NotNull String endpoint, @NotNull String method, @Nullable Map<String, String> params) throws RuntimeException {
        validateInput(endpoint, method);
        Request request = buildRequest(endpoint, method, params);
        JsonElement response = executeRequest(request, params);

        if (response.isJsonObject() && response.getAsJsonObject().has("errors")) {
            this.errors = response.getAsJsonObject().get("errors").toString();
        }

        return response;
    }

    private void validateInput(String endpoint, String method) {
        if (endpoint.isEmpty()) {
            logger.error("Endpoint cannot be empty");
            throw new IllegalArgumentException("Endpoint cannot be empty");
        }

        if (!method.equals("GET") && !method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")) {
            logger.error("Invalid method. Allowed methods: GET, POST, PUT, DELETE");
            throw new IllegalArgumentException("Invalid method. Allowed methods: GET, POST, PUT, DELETE");
        }
    }

    private Request buildRequest(String endpoint, String method, Map<String, String> params) {
        return new Request.Builder()
                .url(urlBuilder(endpoint, params))
                .method(method, null)
                .addHeader("x-apisports-key", KEY)
                .addHeader("x-apisports-host", HOST)
                .build();
    }

    private String urlBuilder(String endpoint, @Nullable Map<String, String> params) {
        try {
            URIBuilder builder = new URIBuilder(API_URL + endpoint);

            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            return builder.build().toString();
        } catch (URISyntaxException e) {
            logger.error("URL building failed for endpoint {}", endpoint, e);
            throw new IllegalArgumentException("Invalid URL or parameters");
        }
    }

    private JsonElement executeRequest(Request request, Map<String, String> params) {
        try (Response response = client.newCall(request).execute()) {

            validateResponse(response, params);

            this.responseHeaders = response.headers();
            this.responseCode = response.code();

            return parseResponseBody(response.body());

        } catch (IOException e) {
            throw new RuntimeException("Failed to send request: " + e.getMessage(), e);
        }
    }

    private void validateResponse(Response response, Map<String, String> params) throws IOException {
        if (!response.isSuccessful()) {
            logger.error("Unexpected code {}", response);
            throw new IOException("Unexpected code " + response);
        }

        int code = response.code();
        if (code != 200) {
            String errorMessage = "Unexpected response code %s for params %s".formatted(code, params != null ?
                    params.toString() : "no params provided");
            logger.debug(errorMessage);
            throw new IOException(errorMessage);
        }
    }

    private JsonElement parseResponseBody(ResponseBody responseBody) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(responseBody.byteStream(), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonElement.class);
        } catch (Exception e) {
            logger.error("Failed to parse JSON stream", e);
            throw new IOException("Failed to parse JSON stream", e);
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public @Nullable String getErrors() {
        return errors;
    }

    public @Nullable Map<String, List<String>> getResponseHeaders() {
        return responseHeaders != null ? this.responseHeaders.toMultimap() : null;
    }
}