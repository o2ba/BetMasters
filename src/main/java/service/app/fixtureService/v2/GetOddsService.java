package service.app.fixtureService.v2;


import java.util.List;
import java.util.Map;

public interface GetOddsService {
    List<Map<String, Double>> getOdds(String fixtureId, int oddID)
    throws Exception;
}
