package service.app.fixtureService.v2.leagues.request;

import service.app.fixtureService.v2.common.exception.FootballApiException;
import service.app.fixtureService.v2.common.model.FootballResponse;

/**
 * Interface for getting leagues
 */
public interface GetLeaguesDao {
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
    FootballResponse getLeagues(String countryCode, boolean activeOnly) throws FootballApiException;


    /**
     * Get league by league ID
     */
    FootballResponse getLeagueById(int leagueId) throws FootballApiException;

}
