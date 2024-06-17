package common.model.football.fixture;

import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Fixture {

    @SerializedName("fixture")
    private FixtureDetails fixtureDetails;

    @SerializedName("league")
    private LeagueDetails leagueDetails;

    /**
     * Summary of the fixture in JSON format.
     * @return JSONObject containing the summary of the fixture details
     */
    public JSONObject JSONSummary() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("league", leagueDetails.name());
        json.put("fixture_id", fixtureDetails.fixtureId());
        json.put("fixture_date", fixtureDetails.date());
        json.put("home_team", fixtureDetails.homeTeam());
        json.put("away_team", fixtureDetails.awayTeam());
        json.put("home_team_icon", fixtureDetails.homeTeamIcon());
        json.put("away_team_icon", fixtureDetails.awayTeamIcon());
        json.put("home_goals", fixtureDetails.homeGoals());
        json.put("away_goals", fixtureDetails.awayGoals());
        json.put("home_penalty_goals", fixtureDetails.homePenaltyGoals());
        json.put("away_penalty_goals", fixtureDetails.awayPenaltyGoals());
        json.put("status_long", fixtureDetails.status().longStatus());
        json.put("status_short", fixtureDetails.status().shortStatus());
        json.put("status_elapsed", fixtureDetails.status().elapsed());
        json.put("odds", fixtureDetails.odds());
        return summary;
    }
}
