package request;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import okhttp3.*;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * The ApiRequest class is used to send requests to the API. It builds the URL for the request and sends the request
 * using the OkHttpClient library. The response is returned as a JsonElement.
 */
public final class ApiRequest {

    /** Logger for the ApiRequest class */
    private static final Logger logger = LoggerFactory.getLogger(ApiRequest.class);

    /** The OkHttpClient object used to send requests. We use a static one here to avoid creating multiple instances of
     * the OkHttpClient object, as OkHttp supports connection pooling
     */
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * The base URL for the API. This URL will be identical for all instances of the ApiRequest class
     */
    private static final String API_URL = "https://v3.football.api-sports.io/";

    /**
     * The API key used to authenticate requests. If no token is retrieved, a RuntimeException is called
     * This token will be identical for all instances of the ApiRequest class
     */
    @NotNull
    private static final String KEY;

    /**
     * The host URL for the API. This URL will be identical for all instances of the ApiRequest class
     */
    private static final String HOST = "v3.football.java.backend.model.api-sports.io";

    /** The response headers after sending a request */
    @Nullable
    Headers responseHeaders;

    /** The HTTP response code after sending a request. 0 by default */
    private int responseCode = 0;

    /** Errors returned within the JSON response by the API */
    @Nullable
    private String errors;


    // -------- Static Initialization Block -------- //

    static {
        try {
            KEY = getToken();
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError("Failed to initialize API key: " + e.getMessage());
        }
    }

    // -------- Static Methods -------- //

    /**
     * Retrieves and validates the API Key from the environment variables.
     * @return the API key
     * @throws IllegalStateException if the API key is not found or is invalid
     */
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

    /**
     * Static method that calls the API to validate the token. If the token is valid, the method returns true. If the
     * token is invalid, the method returns false. A token is considered valid if the API returns a successful response
     * and the "errors" field in the JSON response does not contain a "token" field. Does not throw any exceptions -
     * returns false if the API call fails.
     * @return true if the token is valid, false if the token is invalid
     */
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


            if (jsonObject.has("errors") && jsonObject.get("errors").isJsonArray()) {
                JsonArray errorsArray = jsonObject.get("errors").getAsJsonArray();
                for (JsonElement errorElement : errorsArray) {
                    JsonObject errorObject = errorElement.getAsJsonObject();
                    if (errorObject.has("token")) {
                        return true;
                    }
                }
            } else if (jsonObject.has("errors") && jsonObject.get("errors").isJsonObject()) {
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

    // -------- Getters -------- //

    /**
     * Returns the HTTP response code after sending a request
     * @return the HTTP response code as an integer, or 0 if no request has been sent
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Returns the errors returned within the JSON response by the API
     * @return the errors as a String or null if no request has been sent
     */
    @SuppressWarnings("unused")
    public @Nullable String getErrors () {
        return errors;
    }

    /**
     * Returns the response headers after sending a request
     * @return the response headers as a Map<String, List<String>> or null if no request has been sent.
     */
    public @Nullable Map<String, List<String>> getResponseHeaders() {
        return responseHeaders != null ? this.responseHeaders.toMultimap() : null;
    }

    // -------- sendRequest method and helper methods -------- //

    /**
     * Builds the URL for the request by appending the endpoint and parameters
     * @param endpoint the endpoint to which the request is sent to. Example: "fixtures", "odds"
     * @param params the parameters for the request as a Map<String, String>. Can be null if no parameters are needed.
     *               Example: Map.of("league", "39", "season", "2021")
     * @return the URL as a String which can be used to send the request
     */
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

    /**
     * <p>Sends a request to the API and returns the response as a JsonElement</p>
     * This function will: <ol>
     *       <li>Validate the input parameters</li>
     *       <li>Build the request</li>
     *       <li>Execute the request</li>
     *       <li>Validate the response</li>
     *       <li>Parse the response body</li>
     *       </ol>
     * @see #validateInput(String, String)
     * @see #buildRequest(String, String, Map)
     * @see #executeRequest(Request, Map)
     * @see #validateResponse(Response, Map)
     * @see #parseResponseBody(ResponseBody)
     * @param endpoint the endpoint to which the request is sent to. Example: "fixtures", "odds"
     * @param method the HTTP method to use. Example: "GET", "POST"
     * @param params the parameters for the request as a Map<String, String>. Can be null if no parameters are needed.
     * @return the response as a JsonElement
     * @throws RuntimeException if the request fails due to an IOException
     * @throws IllegalArgumentException if the endpoint is empty or the method is invalid
     */
    public JsonElement sendRequest(@NotNull String endpoint, @NotNull String method,
                                   @Nullable Map<String, String> params) throws RuntimeException {


        validateInput(endpoint, method);
        Request request = buildRequest(endpoint, method, params);
        JsonElement response = executeRequest(request, params);

        if (response.isJsonObject() && response.getAsJsonObject().has("errors")) {
            this.errors = response.getAsJsonObject().get("errors").toString();
            if(!this.errors.equals("[]")) {
                System.out.println("Errors: " + this.errors);
            }
        }


        return response;

    }

    /**
     * Validates the input parameters for the sendRequest method.
     * @param endpoint the endpoint to which the request is sent to.
     * @param method the HTTP method to use.
     * @throws IllegalArgumentException if the endpoint is empty or the method is invalid
     */
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

    /**
     * Builds the request to be sent to the API.
     * @param endpoint the endpoint to which the request is sent to.
     * @param method the HTTP method to use.
     * @param params the parameters for the request as a Map<String, String>.
     * @return the built Request object
     */
    private Request buildRequest(String endpoint, String method, Map<String, String> params) {
        return new Request.Builder()
                .url(urlBuilder(endpoint, params))
                .method(method, null)
                .addHeader("x-apisports-key", KEY)
                .addHeader("x-apisports-host", HOST)
                .build();
    }

    /**
     * Executes the request and returns the response as a JsonElement.
     * @param request the Request object to be sent
     * @param params the parameters for the request as a Map<String, String>.
     * @return the response as a JsonElement
     * @throws RuntimeException if the request fails due to an IOException
     */
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

    /**
     * Validates the response from the API.
     * @param response the Response object from the API
     * @param params the parameters for the request as a Map<String, String>.
     * @throws IOException if the response is not successful or the response code is not 200
     */
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

    /**
     * Parses the response body from the API and returns it as a JsonElement.
     * @param responseBody the ResponseBody object from the API
     * @return the response body as a JsonElement
     * @throws IOException if the response body is null, empty, or cannot be parsed into a JsonElement
     */
    private JsonElement parseResponseBody(ResponseBody responseBody) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(responseBody.byteStream(), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, JsonElement.class);
        } catch (Exception e) {
            logger.error("Failed to parse JSON stream", e);
            throw new IOException("Failed to parse JSON stream", e);
        }
    }

}