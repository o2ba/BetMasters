package dto.httpRequest.interfaces;

import dto.httpRequest.exception.RequestSendingException;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Interface for sending HTTP requests.</p>
 * <p>
 * This interface defines a method for sending HTTP requests. The method takes a Request __object as a parameter,
 * which can be built using an ApiRequestBuilder. The method returns a Response __object which represents the HTTP
 * response received from the server.
 * </p>
 * <p>This interface should be implemented by any class that is responsible for sending HTTP requests.</p>
 */
public interface RequestSender {
    /**
     * Sends an HTTP request to the specified endpoint using the Request __object provided.
     *
     * @param request The Request __object representing the HTTP request to be sent.
     * @return A Response __object representing the HTTP response received.
     * @throws RequestSendingException If an error occurs while sending the request.
     */
    Response sendRequest(Request request) throws RequestSendingException;
}
