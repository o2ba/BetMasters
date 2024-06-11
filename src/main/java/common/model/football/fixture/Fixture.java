package common.model.football.fixture;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Fixture {

    @SerializedName("fixture")
    private FixtureDetails fixtureDetails;

    @Override
    public String toString() {
        return "Fixture{" +
                "fixtureDetails=" + fixtureDetails +
                '}';
    }

}
