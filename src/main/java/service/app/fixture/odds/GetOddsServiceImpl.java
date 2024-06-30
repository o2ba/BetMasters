package service.app.fixture.odds;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.fixture.common.model.FootballResponse;
import service.app.fixture.odds.request.GetOddsDao;


import java.util.HashMap;
import java.util.Map;


/**
 * Gets the odds of a fixture.
 */
@Service
public class GetOddsServiceImpl implements GetOddsService {

    GetOddsDao getOdds;

    public GetOddsServiceImpl(GetOddsDao getOdds) {
        this.getOdds = getOdds;
    }

    @Override
    public Map<String, Double> getOdds(int fixtureId, int oddID) throws Exception {

        FootballResponse footballResponse = getOdds.getOdds(fixtureId, oddID);
        assert footballResponse != null;

        Map<String, Double> oddsMap = new HashMap<>();

        try {
            JsonArray values = footballResponse
                    .response()
                    .get(0)
                    .getAsJsonObject()
                    .get("bookmakers")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("bets")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("values")
                    .getAsJsonArray();

            for (int i = 0; i < values.size(); i++) {
                JsonObject object = values.get(i).getAsJsonObject();
                String value = object.get("value").getAsString();
                Double odd = object.get("odd").getAsDouble();
                oddsMap.put(value, odd);
            }

        } catch (Exception e) {
            throw new Exception("Error while getting odds for fixture" + e.getMessage());
        }

        return oddsMap;
    }


    @Override
    public Map<Integer, Map<String, Double>> getOddsForLeagueAndSeason(int leagueId, int season, int oddID) throws Exception {
        FootballResponse footballResponse = getOdds.getOddsForLeagueAndSeason(leagueId, season, oddID);

        if (footballResponse == null || footballResponse.results() == 0) {
            throw new FixtureNotFoundException("No odds found for this league and season");
        }

        Map<Integer, Map<String, Double>> oddsMap = new HashMap<>();

        for (JsonElement o : footballResponse.response().getAsJsonArray()) {

            int fixtureId = o.getAsJsonObject().get("fixture").getAsJsonObject().get("id").getAsInt();

            JsonArray values = o.getAsJsonObject()
                    .get("bookmakers")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("bets")
                    .getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("values")
                    .getAsJsonArray();

            Map<String, Double> odds = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                JsonObject object = values.get(i).getAsJsonObject();
                String value = object.get("value").getAsString();
                Double odd = object.get("odd").getAsDouble();
                odds.put(value, odd);
            }

            oddsMap.put(fixtureId, odds);

        }


        return oddsMap;
    }
}
