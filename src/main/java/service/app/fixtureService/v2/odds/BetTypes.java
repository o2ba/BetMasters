package service.app.fixtureService.v2.odds;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Here would be a list of all the possible bet types that a user can place
 * For now, we only support home-draw-away bets
 **/
public enum BetTypes {
    HOME_DRAW_AWAY("WIN", "Match Winner", Arrays.asList("home", "draw", "away"), 1),
    HOME_AWAY("HA", "Home/Away", Arrays.asList("home", "away"), 2),
    BOTH_TEAMS_TO_SCORE("BTTS", "Both Teams To Score", Arrays.asList("yes", "no"), 8);
    // ADD MORE BET TYPES HERE

    private final String shortName;
    private final String longName;
    private final List<String> possibleValues;
    private final int id;

    BetTypes(String shortName, String longName, List<String> possibleValues, int id) {
        this.shortName = shortName;
        this.longName = longName;
        this.possibleValues = possibleValues;
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public int getId() {
        return id;
    }


    public List<String> getPossibleValues() {
        return possibleValues;
    }

}
