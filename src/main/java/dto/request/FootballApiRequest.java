package dto.request;

import com.google.gson.JsonElement;
import dto.request.httpRequest.exception.InvalidMethodException;
import dto.request.httpRequest.exception.RequestSendingException;
import dto.request.httpRequest.exception.ResponseParseException;
import dto.request.httpRequest.interfaces.RequestBuilder;
import dto.request.httpRequest.interfaces.RequestSender;
import dto.request.httpRequest.interfaces.ResponseParser;
import dto.response.model.FootballResponsePayload;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.Map;


/**
 * This class represents a request to the Football API.
 * <br>
 * This class is responsible for sending requests to the Football API. It contains the URL and API key needed to send
 * requests to the API.
 * <br>
 * This class is annotated with @Value, which is used to inject values from the application.properties file. The values
 * are injected into the fields footballApiUrl and footballApiKey.
 */
@Service
@SuppressWarnings("SpellCheckingInspection")
public final class FootballApiRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FootballApiRequest.class);

    @Value("${football.api.url}")
    private String footballApiUrl;

    @Value("${football.api.key}")
    private String footballApiKey;

    private final RequestBuilder requestBuilder;
    private final RequestSender requestSender;
    private final ResponseParser<JsonElement> responseParser;


    @Autowired
    public FootballApiRequest(RequestBuilder requestBuilder,
                              RequestSender requestSender,
                              ResponseParser<JsonElement> responseParser) {
        this.requestBuilder = requestBuilder;
        this.requestSender = requestSender;
        this.responseParser = responseParser;
    }

    /**
     * Sends a request to the Football API and returns the parsed response.
     * <p>
     * This method is responsible for sending a request to the Football API. It uses the {@link RequestBuilder} to
     * build the request,
     * the {@link RequestSender} to send the request, and the {@link ResponseParser} to parse the response.
     * </p>
     * <p>
     * The method takes the following parameters:
     * <ul>
     *     <li>endpoint: The API endpoint to which the request will be sent. This should be a valid URL path.</li>
     *     <li>method: The HTTP method to be used for the request. This should be one of the standard HTTP methods such
     *     as "GET", "POST", "PUT", "DELETE", etc.</li>
     *     <li>params: A map containing the parameters to be included in the request. The keys represent the parameter
     *     names and the values represent the parameter values.</li>
     *     <li>body: The request body to be included in the request. This should be a RequestBody __object.</li>
     * </ul>
     * </p>
     * <p>
     * The method returns a parsed __object of type {@link JsonElement} which represents the parsed response.
     * </p>
     *
     * @param endpoint The API endpoint to which the request will be sent. This should be a valid URL path.
     * @param method The HTTP method to be used for the request. This should be one of the standard HTTP methods such
     *               as "GET", "POST", "PUT", "DELETE", etc.
     * @param params A map containing the parameters to be included in the request. The keys represent the parameter
     *               names and the values represent the parameter values.
     * @param body The request body to be included in the request. This should be a RequestBody __object.
     * @return The parsed __object. This is an __object of type {@link JsonElement} which represents the parsed response.
     * @throws ResponseParseException If there's an error while parsing the response.
     * @throws InvalidMethodException If the provided method is not a valid HTTP method.
     * @throws RequestSendingException If there's an error while sending the request.
     */
    public FootballResponsePayload sendRequest(String endpoint,
                                               String method,
                                               Map<String, String> params,
                                               RequestBody body)
    throws ResponseParseException, InvalidMethodException, RequestSendingException {

        Map<String, String> headers = Map.of("x-rapidapi-key", footballApiKey,
                                             "x-rapidapi-host", "v3.football.api-sports.io");


        Request request = requestBuilder.buildRequest(footballApiUrl, endpoint, method, params, body, headers);


        try (Response response = requestSender.sendRequest(request)) {
            return new FootballResponsePayload(response, responseParser.parseResponse(response));
        } catch (ResponseParseException e) {
            LOGGER.error("Error parsing response", e);
            throw new ResponseParseException("Error parsing response", e);
        } catch (RequestSendingException e) {
            LOGGER.error("Error sending request", e);
            throw new RequestSendingException("Error sending request", e);
        } catch (Exception e) {
            LOGGER.error("Error", e);
            throw new RequestSendingException("Error", e);
        }

    }

}