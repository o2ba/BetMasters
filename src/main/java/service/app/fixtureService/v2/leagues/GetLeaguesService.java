package service.app.fixtureService.v2.leagues;


import service.app.fixtureService.v2.common.exception.FootballApiException;

import java.util.List;
import java.util.Map;

public interface GetLeaguesService {
    List<Map<String, Object>> getLeagues(String country, boolean current)
    throws FootballApiException;
}
