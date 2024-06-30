package service.general.external.apiService;

import com.google.gson.JsonElement;

import java.util.Map;

/**
 * ApiRequest interface defines the contract for sending API requests.
 */
public interface ApiRequest {

    /**
     * Sends an API request to the specified endpoint with the given method and parameters.
     *
     * @param endpoint the endpoint to which the request is sent. Example: "fixtures", "odds"
     * @param method the HTTP method to use. Example: "GET", "POST"
     * @param params the parameters for the request as a Map<String, String>. Can be null if no parameters are needed.
     * @return the response as a JsonElement
     * @throws RuntimeException if the request fails due to an IOException
     * @throws IllegalArgumentException if the endpoint is empty or the method is invalid
     */
    JsonElement sendRequest(String endpoint, String method, Map<String, String> params) throws RuntimeException;
}
