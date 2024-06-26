package service.app.fixtureService.v2.odds.dao;

import com.google.gson.JsonElement;
import dto.ApiRequest;
import dto.httpRequest.exception.RequestSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import service.app.fixtureService.v2.common.model.FootballResponse;

import java.util.Map;

@Service
public class GetOddsDaoImpl implements GetOddsDao {

    ApiRequest apiRequest;

    @Autowired
    public GetOddsDaoImpl(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    @Value("${bookmaker}")
    private int bookmaker;

    private String apiKey = System.getenv("API_KEY");

    /**
     * Retrieve odds for a fixture. Uses the default bookmaker
     *
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID     The ID of the odd to get
     * @return Map<String, Double>
     */
    @Override
    public FootballResponse getOdds(String fixtureId, int oddID) throws RequestSendingException {

        System.out.println();

        return getOdds(fixtureId, oddID, String.valueOf(bookmaker));
    }

    /**
     * Retrieve odds for a fixture
     *
     * @param fixtureId The ID of the fixture to get the odds for
     * @param oddID     The ID of the odd to get
     * @param bookmaker The bookmaker to get the odds from
     * @return Map<String, Double>
     */
    @Override
    public FootballResponse getOdds(String fixtureId, int oddID, String bookmaker) throws RequestSendingException {
        Map<String, String> params = Map.of("fixture", fixtureId, "bet", String.valueOf(oddID), "bookmaker",
                bookmaker);

        JsonElement response = apiRequest.sendRequest("odds", "GET", params);

        return new FootballResponse(response.getAsJsonObject().get("response").getAsJsonArray(),
                response.getAsJsonObject().get("errors").getAsJsonArray(),
                response.getAsJsonObject().get("results").getAsInt());
    }
}
