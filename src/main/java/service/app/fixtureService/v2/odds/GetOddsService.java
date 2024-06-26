package service.app.fixtureService.v2.odds;


import java.util.Map;

public interface GetOddsService {
    Map<String, Double> getOdds(String fixtureId, int oddID)
    throws Exception;
}
