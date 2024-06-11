package common.model.football.fixture.enums;

public enum FixtureStatus {
    TBD("TBD", "Time To Be Defined", "Scheduled", "Scheduled but date and time are not known"),
    NS("NS", "Not Started", "Scheduled", ""),
    _1H("1H", "First Half, Kick Off", "In Play", "First half in play\"),\n" +
            "    HT(\"HT\", \"Halftime\", \"In Play\", \"Finished in the regular ti"),
    _2H("2H", "Second Half, 2nd Half Started", "In Play", "Second half in play"),
    ET("ET", "Extra Time", "In Play", "Extra time in play"),
    BT("BT", "Break Time", "In Play", "Break during extra time"),
    P("P", "Penalty In Progress", "In Play", "Penalty played after extra time"),
    SUSP("SUSP", "Match Suspended", "In Play", "Suspended by referee's decision, may be rescheduled another day"),
    INT("INT", "Match Interrupted", "In Play", "Interrupted by referee's decision, should resume in a few minutes"),
    FT("FT", "Match Finished", "Finished", "Finished in the regular time"),
    AET("AET", "Match Finished", "Finished", "Finished after extra time without going to the penalty shootout"),
    PEN("PEN", "Match Finished", "Finished", "Finished after the penalty shootout"),
    PST("PST", "Match Postponed", "Postponed", "Postponed to another day, once the new date and time is known the status will change to Not Started"),
    CANC("CANC", "Match Cancelled", "Cancelled", "Cancelled, match will not be played"),
    ABD("ABD", "Match Abandoned", "Abandoned", "Abandoned for various reasons (Bad Weather, Safety, Floodlights, Playing Staff Or Referees), Can be rescheduled or not, it depends on the competition"),
    AWD("AWD", "Technical Loss", "Not Played", ""),
    WO("WO", "WalkOver", "Not Played", "Victory by forfeit or absence of competitor"),
    LIVE("LIVE", "In Progress", "In Play", "Used in very rare cases. It indicates a fixture in progress but the data indicating the half-time or elapsed time are not available");

    private final String code;
    private final String name;
    private final String category;
    private final String description;

    FixtureStatus(String code, String name, String category, String description) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
