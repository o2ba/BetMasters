package service.app.fixture.v2.odds;


import java.util.Map;

public interface GetOddsService {
    Map<String, Double> getOdds(int fixtureId, int oddID)
    throws Exception;

    Map<Integer, Map<String, Double>> getOddsForLeagueAndSeason(int leagueId, int season, int oddID)
    throws Exception;
}
