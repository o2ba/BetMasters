package common.model.football.fixture;

import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * This class represents the details of a fixture.
 * Without this method, certain fields will be null.
 */
public record FixtureDetails(
        @SerializedName("id") Integer fixtureId,
        @SerializedName("date") String date,
        @SerializedName("timestamp") Long timestamp,
        @SerializedName("periods") Periods periods,
        @SerializedName("status") Status status
)
{

    private record Status(
            @SerializedName("short") String shortStatus,
            @SerializedName("elapsed") Integer elapsed
    ) { }



    private record Periods(
            @SerializedName("first") Long firstPeriod,
            @SerializedName("second") Long secondPeriod
    ) { }

    public LocalDateTime getDateAsTimestamp() {
        return convertTimestampToDate(timestamp);
    }

    private LocalDateTime convertTimestampToDate(Long timestamp) {
        if (timestamp != null) {
            Instant instant = Instant.ofEpochSecond(timestamp);
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return null;
    }

    @Override
    public String toString() {
        return "FixtureDetails{" +
                "fixtureId=" + fixtureId +
                ", date='" + getDate() + '\'' +
                ", timestamp=" + timestamp +
                ", periods=" + periods +
                ", status=" + status +
                '}';
    }
}
