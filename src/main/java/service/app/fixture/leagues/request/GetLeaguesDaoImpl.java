package service.app.fixture.leagues.request;

import com.google.gson.JsonElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixture.v2.common.exception.FootballApiException;
import service.app.fixture.v2.common.model.FootballResponse;
import service.general.external.apiService.ApiRequest;

import java.util.Map;


@Service
public class GetLeaguesDaoImpl implements GetLeaguesDao {


    ApiRequest apiRequest;

    @Autowired
    public GetLeaguesDaoImpl(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    private String apiKey = System.getenv("API_KEY");

    /**
     * Get all leagues by country code
     * @param countryCode The country code to get leagues for (e.g. "GB" for Great Britain)
     * @param activeOnly Whether to get only active leagues
     * @return FootballResponse
     * @throws FootballApiException if the request fails
     *
     * <p>Sample output:</p>
     * <pre>
     * [<br>
     *    {<br>
     *    "country": "England",<br>
     *    "flag": "https://media.api-sports.io/flags/gb.svg",<br>
     *    "name": "Premier League",<br>
     *    "logo": "https://media.api-sports.io/leagues/2.png",<br>
     *    "current": true,<br>
     *    "season": "2023"<br>
     *    },<br>
     *    ...<br>
     * ]<br>
     *
     */
    @Override
    public FootballResponse getLeagues(String countryCode, boolean activeOnly) throws FootballApiException {

        String active = activeOnly ? "true" : "false";

        Map<String, String> params = Map.of("country", countryCode, "current", active);

        JsonElement response = apiRequest.sendRequest("leagues", "GET", params);

        return new FootballResponse(response.getAsJsonObject().get("response").getAsJsonArray(),
                response.getAsJsonObject().get("errors").getAsJsonArray(),
                response.getAsJsonObject().get("results").getAsInt());
    }

    /**
     * Get league by league ID
     *
     * @param leagueId
     */
    @Override
    public FootballResponse getLeagueById(int leagueId) throws FootballApiException {
        Map<String, String> params = Map.of("id", String.valueOf(leagueId));

        JsonElement response = apiRequest.sendRequest("leagues", "GET", params);

        return new FootballResponse(response.getAsJsonObject().get("response").getAsJsonArray(),
                response.getAsJsonObject().get("errors").getAsJsonArray(),
                response.getAsJsonObject().get("results").getAsInt());
    }
}
