package dto.request.httpRequest.interfaces;

import dto.request.httpRequest.exception.InvalidMethodException;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public interface RequestBuilder {

    /**
     * Builds an HTTP request using the provided endpoint, method, and parameters.
     *
     * @param url The base URL to which the request will be sent. This should be a valid URL.
     * @param endpoint The API endpoint to which the request will be sent. This should be a valid URL path.
     * @param method The HTTP method to be used for the request. This should be one of the standard HTTP methods such as "GET", "POST", "PUT", "DELETE", etc.
     * @param params A map containing the parameters to be included in the request. The keys represent the parameter names and the values represent the parameter values.
     * @return A Request __object representing the built HTTP request.
     * @throws InvalidMethodException If the provided method is not a valid HTTP method.
     */
    Request buildRequest(String url,
                         String endpoint,
                         String method,
                         Map<String, String > params,
                         RequestBody body,
                         Map<String, String> headers)
    throws InvalidMethodException;
}
