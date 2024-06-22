package dto.request.httpRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import dto.request.httpRequest.exception.ResponseParseException;
import dto.request.httpRequest.interfaces.ResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GsonResponseParser implements ResponseParser<JsonElement> {


    private final Gson gson;

    @Autowired
    public GsonResponseParser(Gson gson) {
        this.gson = gson;
    }

    /**
     * Parses an HTTP response and returns the parsed __object.
     * <br>
     * This method parses an HTTP response and returns the parsed __object. The response is represented by a Response
     * __object, which represents the HTTP response received from a server. The method returns a parsed __object of type T.
     * <br>
     * @param response The response to be parsed. This should be a Response __object representing the HTTP response.
     * @return The parsed __object. This is an __object of type T which represents the parsed response.
     * @throws ResponseParseException If there's an error while parsing the response.
     */
    @Override
    public JsonElement parseResponse(okhttp3.Response response) throws ResponseParseException {
        if (response.body() == null) {
            throw new ResponseParseException("Response body is null");
        }
        return gson.fromJson(response.body().charStream(), JsonElement.class);
    }

}
