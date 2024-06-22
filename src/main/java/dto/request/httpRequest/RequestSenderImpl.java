package dto.request.httpRequest;

import dto.request.httpRequest.exception.RequestSendingException;
import dto.request.httpRequest.interfaces.RequestSender;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class RequestSenderImpl implements RequestSender {

    private final OkHttpClient client = new OkHttpClient();

    /**
     * Sends an HTTP request to the specified endpoint using the Request __object provided.
     *
     * @param request The Request __object representing the HTTP request to be sent.
     * @return A Response __object representing the HTTP response received.
     * @throws RequestSendingException If an error occurs while sending the request.
     */
    @Override
    public Response sendRequest(Request request) throws RequestSendingException {
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            throw new RequestSendingException("Error sending request", e);
        }
    }
}