package service.app.fixture.leagues;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.common.model.FootballResponse;
import service.app.fixture.leagues.request.GetLeaguesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetLeaguesServiceImpl implements GetLeaguesService {

    private GetLeaguesDao getLeaguesDao;

    @Autowired
    public GetLeaguesServiceImpl(GetLeaguesDao getLeaguesDao) {
        this.getLeaguesDao = getLeaguesDao;
    }

    @Override
    public List<Map<String, Object>> getLeagues(String country, boolean current) throws FootballApiException {

        List<Map<String, Object>> leaguesOutput = new ArrayList<>();

        try {
            FootballResponse leagues = getLeaguesDao.getLeagues(country, current);

            JsonArray leaguesArray = leagues.response().getAsJsonArray();

            System.out.println(leagues.errors());

            for (int i = 0; i < leaguesArray.size(); i++) {
                JsonObject leagueObject = leaguesArray.get(i).getAsJsonObject();
                JsonObject league = leagueObject.get("league").getAsJsonObject();
                JsonObject countryObj = leagueObject.get("country").getAsJsonObject();

                if (league.has("season") && league.get("season").isJsonArray()) {
                    JsonArray seasons = league.get("season").getAsJsonArray();

                    // loop through the seasons, find the one that is current
                    for (JsonElement seasonElement : seasons) {
                        JsonObject season = seasonElement.getAsJsonObject();
                        if (season.get("current").getAsBoolean()) {
                            league.addProperty("season", season.get("year").getAsString());
                            break;
                        }
                    }
                }

                leaguesOutput.add(Map.of(
                        "league_id", league.get("id").getAsInt(),
                        "league_name", league.get("name").getAsString(),
                        "league_type", league.get("type").getAsString(),
                        "league_logo", league.get("logo").getAsString(),
                        "country_name", countryObj.get("name").getAsString(),
                        "country_flag", countryObj.get("flag").getAsString(),
                        "country_code", countryObj.get("code").getAsString()
                ));
            }

        } catch (FootballApiException e) {
            throw new FootballApiException("Error getting leagues");
        }

        return leaguesOutput;
    }
}