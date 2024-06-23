package dto.httpRequest;

import dto.httpRequest.exception.InvalidMethodException;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;
import dto.httpRequest.interfaces.RequestBuilder;

import java.util.List;
import java.util.Map;

@Component
public class ApiRequestBuilder implements RequestBuilder {

    /**
     * Builds an HTTP request using the provided endpoint, method, and parameters.
     *
     * @param endpoint The API endpoint to which the request will be sent. This should be a valid URL path.
     * @param method   The HTTP method to be used for the request. This should be one of the standard HTTP methods such as "GET", "POST", "PUT", "DELETE", etc.
     * @param params   A map containing the parameters to be included in the request. The keys represent the parameter names and the values represent the parameter values.
     * @param body     The request body to be included in the request. This should be a RequestBody __object.
     * @return A Request __object representing the built HTTP request.
     * @throws InvalidMethodException    If the provided method is not a valid HTTP method.
     */
    @Override
    public Request buildRequest(String url, String endpoint, String method, Map<String, String> params,
                                RequestBody body, Map<String, String> headers)
    throws InvalidMethodException {

        List<String> validMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS");

        if (!validMethods.contains(method)) {
            throw new InvalidMethodException("Invalid HTTP method: " + method);
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        if (!url.endsWith("/") && !endpoint.startsWith("/")) {
            urlBuilder.append("/");
        }
        urlBuilder.append(endpoint);
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(urlBuilder.toString())
                .method(method, body);

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return requestBuilder.build();
    }


}
