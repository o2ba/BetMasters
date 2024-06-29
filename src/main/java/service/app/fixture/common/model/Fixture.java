package service.app.fixture.v2.common.model;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

public record Fixture(
        String league,
        int leagueId,
        int fixtureId,
        String fixtureDate,
        String homeTeam,
        String homeTeamIcon,
        String awayTeam,
        String awayTeamIcon,
        String status,
        int homeGoals,
        int awayGoals,
        int homePenalties,
        int awayPenalties,
        int minutesElapsed
) {

    public Map<String, Object> toMap() {

        Map<String, Object> o = new HashMap<>();

        o.put("league", league);
        o.put("league_id", leagueId);
        o.put("fixture_id", fixtureId);
        o.put("fixture_date", fixtureDate);
        o.put("home_team", homeTeam);
        o.put("home_team_icon", homeTeamIcon);
        o.put("away_team", awayTeam);
        o.put("away_team_icon", awayTeamIcon);
        o.put("status", status);
        o.put("home_goals", homeGoals);
        o.put("away_goals", awayGoals);
        o.put("home_penalties", homePenalties);
        o.put("away_penalties", awayPenalties);
        o.put("minutes_elapsed", minutesElapsed);
        o.put("betting_allowed", bettingAllowed() ? "yes" : "no");

        return o;
    }

    public String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(toMap());
    }

    public JsonElement toJsonElement() {
        return new GsonBuilder().setPrettyPrinting().create().toJsonTree(toMap());
    }

    /**
     * Betting is allowed if the status is NS (Not Started)
     * @return boolean
     */
    public boolean bettingAllowed() {
        return status.equals("NS");
    }

    /**
     * Claiming allowed if the status is FT (Full Time)
     */
    public boolean claimingAllowed() {
        return status.equals("FT");
    }

}