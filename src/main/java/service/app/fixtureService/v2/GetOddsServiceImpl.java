package service.app.fixtureService.v2;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import service.app.fixtureService.v2.dto.FootballResponse;
import service.app.fixtureService.v2.dto.GetOdds;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gets the odds of a fixture.
 */
@Service
public class GetOddsServiceImpl implements GetOddsService {

    GetOdds getOdds;

    public GetOddsServiceImpl(GetOdds getOdds) {
        this.getOdds = getOdds;
    }

    @Override
    public List<Map<String, Double>> getOdds(String fixtureId, int oddID) throws Exception {

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

        return List.of(oddsMap);
    }
}
