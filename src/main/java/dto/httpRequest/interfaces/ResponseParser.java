package dto.httpRequest.interfaces;

import dto.httpRequest.exception.ResponseParseException;
import okhttp3.Response;

/**
 * Interface for parsing HTTP responses.
 *
 * This interface defines a method for parsing HTTP responses. The method takes a Response __object as a parameter,
 * which represents the HTTP response received from a server. The method returns a parsed __object of type T.
 *
 * This interface should be implemented by any class that is responsible for parsing HTTP responses.
 */
public interface ResponseParser<T> {

    /**
     * Parses an HTTP response and returns the parsed __object.
     *
     * This method parses an HTTP response and returns the parsed __object. The response is represented by a Response
     * __object, which represents the HTTP response received from a server. The method returns a parsed __object of type T.
     *
     * @param response The response to be parsed. This should be a Response __object representing the HTTP response.
     * @return The parsed __object. This is an __object of type T which represents the parsed response.
     * @throws ResponseParseException If there's an error while parsing the response.
     */
    T parseResponse(Response response) throws ResponseParseException;
}