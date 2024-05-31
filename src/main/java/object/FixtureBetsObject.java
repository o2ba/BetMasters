package object;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/* The bets available for a fixture
* Only supports Draw/Home/Away currently
*/
public class FixtureBetsObject {

    public String bookmaker;
    public String fixtureID;
    public List<BetObject> odds;

    public FixtureBetsObject(JsonElement input) {

        JsonObject fixture = input.getAsJsonObject().get("fixture").getAsJsonObject();
        JsonObject league = input.getAsJsonObject().get("league").getAsJsonObject();
        JsonArray bookmakers = input.getAsJsonObject().get("bookmakers").getAsJsonArray();

        // add fixture ID
        this.fixtureID = fixture.get("id").getAsString();

        // The first bookmaker will always be the one selected. Please filter the bookmaker you want to use
        // in your request. Multiple bookmaker selection is not supported as this slows down the API request
        // significantly.
        JsonObject firstBookmaker = bookmakers.get(0).getAsJsonObject();

        // add bookmaker
        this.bookmaker = firstBookmaker.get("name").getAsString();

        // System.out.println(firstBookmaker.get("bets").getAsJsonArray().get(0));

        this.odds = parseBets(firstBookmaker.get("bets").getAsJsonArray());


        // loop through "bookmakers" in fixture

    }


    /**
     * Parses the bets from the API response. Takes the JSON array of bets and returns a list of GameOdds objects.
     * Unsupported bet types will be skipped.
     * @param bets
     * @return A list of GameOdds objects.
     */
    protected List<BetObject> parseBets(JsonArray bets) {

        List<BetObject> gameOdds = new ArrayList<>();

        // iter through bets array
        for (JsonElement bet : bets) {

            // System.out.println(bet);

            // get the bet type
            String betType = bet.getAsJsonObject().get("name").getAsString();

            // check if the bet type is supported
            if (betType.equals("Match Winner")) {
                // get the odds
                JsonArray odds = bet.getAsJsonObject().get("values").getAsJsonArray();
                // 0 = home, 1 = draw, 2 = away
                BetObject matchWinnerOdds = new BetObject(BetObject.betType.HomeDrawAway, Map.of(
                        "home", odds.get(0).getAsJsonObject().get("odd").getAsDouble(),
                        "draw", odds.get(1).getAsJsonObject().get("odd").getAsDouble(),
                        "away", odds.get(2).getAsJsonObject().get("odd").getAsDouble()
                ));

                gameOdds.add(matchWinnerOdds);

            } else if (betType.equals("Both Teams Score")) {
                JsonArray odds = bet.getAsJsonObject().get("values").getAsJsonArray();

                BetObject BothTeamsScore = new BetObject(BetObject.betType.BothTeamsScore, Map.of(
                        "Yes", odds.get(0).getAsJsonObject().get("odd").getAsDouble(),
                        "No", odds.get(1).getAsJsonObject().get("odd").getAsDouble()
                ));

                gameOdds.add(BothTeamsScore);

            }

        }

        return gameOdds;
    }

    public String toFormattedString() {

        // build string for odds, incl their type
        StringBuilder sb = new StringBuilder();

        for (BetObject odd : odds) {
            sb.append(odd.toString()).append(" ");
        }

        try {
            return "FixtureObject ID: " + fixtureID + " | Bookmaker: " + bookmaker + " | Odds: " + odds.toString();
        } catch (NullPointerException e) {
            return "No odds available for fixture: " + fixtureID + " | Bookmaker: " + bookmaker;
        }
    }


    // ------ Getters ------ //
    public String getFixtureId() {
        return fixtureID;
    }

    /**
     * Converts the odds to JSON. The fixture ID is not passed on as it is already available in the fixture object.
     * @return the odds as a JSON object
      */
    public JsonElement toJson() {
        // Create a JsonArray to hold the odds
        JsonArray oddsArray = new JsonArray();

        // Iterate over the odds list
        for (BetObject odd : odds) {
            // Create a JsonObject for each GameOdds
            JsonObject oddObject = new JsonObject();

            // Add the type and odds to the JsonObject
            oddObject.addProperty("type", odd.getType().toString());
            JsonObject oddsObject = new JsonObject();
            for (Map.Entry<String, Double> entry : odd.getOdds().entrySet()) {
                oddsObject.addProperty(entry.getKey(), entry.getValue());
            }
            oddObject.add("odds", oddsObject);

            // Add the JsonObject to the JsonArray
            oddsArray.add(oddObject);
        }

        // Return the JsonArray
        return oddsArray;
    }
}