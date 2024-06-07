package common.object.fixture;


import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

/**
 * A class to represent a fixture in football.
 * This class is used to store information about a fixture, such as the teams playing, the score, the status, and the odds.
 */
public class FixtureObject {

    /** The logger for the FixtureObject class. */
    private static final Logger logger = LoggerFactory.getLogger(FixtureObject.class);

    /** The unique identifier for the fixture. */
    final private @NotNull String fixture_id;

    /** The unique identifier for the league. */
    final private @NotNull String league_id;

    /**
     * The name of the league.
     */
    private final String league_name;

    /** The date of the fixture. */
    final private @NotNull String fixture_date;

    /** The status of the fixture. */
    final private @NotNull Map<String, String> status;

    /** The teams playing in the fixture. */
    final private @NotNull Map<String, String> teams;

    /** The goals scored by each team. */
    final private @NotNull Map<String, Integer> goals;

    /** Penalty goals scored by each team. */
    private final @NotNull Map<String, Integer> penaltyGoals;

    /** The team icons */
    private final @NotNull Map<String, String> teamIcons;

    /** The odds for the fixture. */
    private @Nullable FixtureBetsObject odds;

    // ----------- Constructor and constructor helper functions ------------ //

    /**
     * Constructor for the FixtureObject class.
     * This constructor takes a JsonElement as input and parses the information from it.
     * The JsonElement should be in the format returned by the API
     * @param input the JsonElement to parse
     * @throws JsonParseException if the JsonElement cannot be parsed
     */
    public FixtureObject(JsonElement input) throws JsonParseException {

        JsonObject fixture;
        JsonObject league;
        JsonObject goals;
        JsonObject score;
        JsonObject teams;

        try {
            fixture = input.getAsJsonObject().get("fixture").getAsJsonObject();
            league = input.getAsJsonObject().get("league").getAsJsonObject();
            goals = input.getAsJsonObject().get("goals").getAsJsonObject();
            score = input.getAsJsonObject().get("score").getAsJsonObject();
            teams = input.getAsJsonObject().get("teams").getAsJsonObject();

            this.fixture_id = fixture.get("id").getAsString();
            this.league_id = league.get("id").getAsString();
            this.league_name = league.get("name").getAsString();
            this.fixture_date = fixture.get("date").getAsString();
        } catch (Exception e) {
            logger.error("Could not retrieve fixture information: {}", e.getMessage());
            throw new JsonParseException("Could not retrieve fixture information");
        }

        this.teamIcons = Map.of(
                "home", getAsStringSafe(teams.get("home").getAsJsonObject(), "logo"),
                "away", getAsStringSafe(teams.get("away").getAsJsonObject(), "logo")
        );

        this.status = Map.of(
                "long", getAsStringSafe(fixture.getAsJsonObject().get("status").getAsJsonObject(), "long"),
                "short", getAsStringSafe(fixture.getAsJsonObject().get("status").getAsJsonObject(), "short"),
                "elapsed", getAsStringSafe(fixture.getAsJsonObject().get("status").getAsJsonObject(), "elapsed")
        );

        this.teams = Map.of(
                "home", getAsStringSafe(teams.get("home").getAsJsonObject(), "name"),
                "away", getAsStringSafe(teams.get("away").getAsJsonObject(), "name")
        );

        this.goals = Map.of(
                "home", getAsIntSafe(goals, "home"),
                "away", getAsIntSafe(goals, "away")
        );

        this.penaltyGoals = Map.of(
                "home", getAsIntSafe(score.get("penalty").getAsJsonObject(), "home"),
                "away", getAsIntSafe(score.get("penalty").getAsJsonObject(), "away")
        );

    }

    /**
     * Returns a safe string from a JsonObject. If the key is not present or the value is null, it returns "None".
     * @param jsonObject the JsonObject to get the value from
     * @param key the key to get the value for
     * @return the value as a string
     */
    private String getAsStringSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element == null || element.isJsonNull()) {
            return "None";
        } else {
            return element.getAsString();
        }
    }

    /**
     * Returns a safe integer from a JsonObject. If the key is not present or the value is not a number, it returns 0.
     * @param jsonObject the JsonObject to get the value from
     * @param key the key to get the value for
     * @return the value as an integer
     */
    private Integer getAsIntSafe(JsonObject jsonObject, String key) {
        JsonElement element = jsonObject.get(key);
        if (element == null || !element.isJsonPrimitive() || !((JsonPrimitive) element).isNumber()) {
            return 0;
        } else {
            return element.getAsInt();
        }
    }

    // ----------- Interface & Getters ------------ //

    /**
     * Returns a boolean indicating whether the game is in the future or not.
     * @return true if the game is in the future, false otherwise
     */
    public boolean gameInFuture() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        LocalDateTime fixtureDateTime = LocalDateTime.parse(this.fixture_date, formatter);
        LocalDateTime now = LocalDateTime.now();
        return fixtureDateTime.isAfter(now);
    }

    /**
     * See gameInFuture. This is the opposite.
     * @return true if the game is in the past, false otherwise
     */
    public boolean gameInPast() {
        return !gameInFuture();
    }

    /**
     * Returns a boolean indicating whether the game has odds or not.
     * @return true if the game has odds, false otherwise
     */
    public boolean gameWithOdds() {
        return this.odds != null;
    }

    /**
     * Returns the fixture id. This is a unique identifier for the fixture.
     * @return the fixture id
     */
    public String getFixtureId() {
        return fixture_id;
    }

    /**
     * Returns the league id. This is a unique identifier for the league.
     * @return the league id
     */
    @SuppressWarnings("unused")
    public String getLeagueId() {
        return league_id;
    }

    /**
     * Returns the odds for the fixture. If the odds are not present, it returns an empty Optional.
     * @return the odds for the fixture
     */
    @SuppressWarnings("unused")
    public Optional<FixtureBetsObject> getOdds() {
        return Optional.ofNullable(this.odds);
    }

    // ----------- Setters ------------ //

    /**
     * Sets the odds for the fixture.
     * @param odds the odds for the fixture
     */
    public void setFixtureBets(FixtureBetsObject odds) {
        if (odds == null) {
            logger.warn("Attempted to set null odds for fixture with ID: {}", this.fixture_id);
        }
        this.odds = odds;
    }

    // ----------- JSON Serialization ------------ //

    /**
     * Returns a JSON representation of the fixture.
     * This method creates a JSON object that includes all relevant fixture details,
     * making it easy to serialize fixture data for API responses or data storage.
     *
     * @return the JSON representation of the fixture
     */
    public JsonObject toJson() {
        JsonObject fixtureJson = new JsonObject();

        addBasicDetails(fixtureJson);
        addTeamDetails(fixtureJson);
        addGoalDetails(fixtureJson);
        addStatusDetails(fixtureJson);
        addOddsDetails(fixtureJson);

        return fixtureJson;
    }

    /**
     * Adds basic fixture details to the JSON object.
     * @param json the JSON object to which the basic details are added
     */
    private void addBasicDetails(JsonObject json) {
        json.addProperty("league", league_name);
        json.addProperty("fixture_id", fixture_id);
        json.addProperty("league_id", league_id);
        json.addProperty("fixture_date", fixture_date);
    }

    /**
     * Adds team-related details to the JSON object.
     * @param json the JSON object to which the team details are added
     */
    private void addTeamDetails(JsonObject json) {
        json.addProperty("home_team", teams.get("home"));
        json.addProperty("away_team", teams.get("away"));
        json.addProperty("home_team_icon", teamIcons.get("home"));
        json.addProperty("away_team_icon", teamIcons.get("away"));
    }

    /**
     * Adds goal details to the JSON object.
     * @param json the JSON object to which the goal details are added
     */
    private void addGoalDetails(JsonObject json) {
        json.addProperty("home_goals", goals.get("home"));
        json.addProperty("away_goals", goals.get("away"));
        json.addProperty("home_penalty_goals", penaltyGoals.get("home"));
        json.addProperty("away_penalty_goals", penaltyGoals.get("away"));
    }

    /**
     * Adds status details to the JSON object.
     * @param json the JSON object to which the status details are added
     */
    private void addStatusDetails(JsonObject json) {
        json.addProperty("status_long", status.get("long"));
        json.addProperty("status_short", status.get("short"));
        json.addProperty("status_elapsed", status.get("elapsed"));
    }

    /**
     * Adds odds details to the JSON object.
     * @param json the JSON object to which the odds details are added
     */
    private void addOddsDetails(JsonObject json) {
        if (odds != null) {
            json.add("odds", odds.toJson());
        } else {
            json.addProperty("odds", "No odds available for this fixture");
        }
    }

}