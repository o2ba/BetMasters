package service.app.fixture.leagues;

import service.app.fixture.common.exception.FootballApiException;

import java.util.List;
import java.util.Map;

public interface GetLeaguesService {
    List<Map<String, Object>> getLeagues(String country, boolean current)
    throws FootballApiException;
}
