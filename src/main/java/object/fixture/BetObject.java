package object.fixture;

import java.util.Map;

/*
 * Class for Game Odds. A GameOdds object is a representation of the odds available for a game.
 */
public class BetObject {

    public enum betType {
        HomeAway,
        HomeDrawAway,
        OverUnder,
        BothTeamsScore
    }

    /** The lengths of the different types of odds */
    private final Map<betType, Integer> odd_lengths =
            Map.of(betType.HomeAway, 2, betType.HomeDrawAway, 3, betType.OverUnder, 2);

    /** The type of odds */
    private final betType type;

    /** The odds for the game */
    private Map<String, Double> odds;

    /**
     * Constructor for the GameOdds class
     * @param type the type of odds
     * @param odds the odds for the game
     */
    public BetObject(betType type, Map<String, Double> odds) {
        this.odds = odds;
        this.type = type;
    }

    /**
    * Returns the length of the odds. For example, HomeAway odds have a length of 2
    * @return the length of the odds
    */
    public int getOddLength() {
        return odd_lengths.get(type);
    }

    /**
     * Returns the type of odds
     * @return the type of odds
     */

    public betType getType() {
        return type;
    }

    public Map<String, Double> getOdds() {
        return odds;
    }

    public String toString() {

        // loop through odds and print them in one line
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Double> entry : odds.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
        }

        return type.toString() + "{ " + sb + " }";

    }
}
