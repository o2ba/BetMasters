package dto.request.httpRequest;

import dto.request.httpRequest.exception.InvalidMethodException;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FootballApiRequestBuilderTest {

    private ApiRequestBuilder apiRequestBuilder;

    @BeforeEach
    public void setUp() {
        apiRequestBuilder = new ApiRequestBuilder();
    }

    @Test
    public void testBuildRequest() throws InvalidMethodException {
        // Given
        String url = "https://v3.football.api-sports.io/";
        String endpoint = "teams";
        String method = "GET";
        Map<String, String> params = new HashMap<>();
        params.put("league", "39");
        params.put("season", "2020");
        RequestBody body = null;
        Map<String, String> headers = new HashMap<>();
        headers.put("x-rapidapi-key", "YOUR_RAPIDAPI_KEY");
        headers.put("x-rapidapi-host", "v3.football.api-sports.io");

        // When
        Request request = apiRequestBuilder.buildRequest(url, endpoint, method, params, body, headers);

        // Then
        assertNotNull(request);
        assertEquals("GET", request.method());
        assertTrue(request.url().toString().contains("league=39"));
        assertTrue(request.url().toString().contains("season=2020"));
        assertEquals("YOUR_RAPIDAPI_KEY", request.header("x-rapidapi-key"));
        assertEquals("v3.football.api-sports.io", request.header("x-rapidapi-host"));
    }
}