package common.model.football.fixture;

import com.google.gson.annotations.SerializedName;

public record LeagueDetails(
        @SerializedName("id") Integer leagueId,
        @SerializedName("name") String name,
        @SerializedName("country") String country,
        @SerializedName("season") String season,
        @SerializedName("logo") String logo,
        @SerializedName("flag") String flag
)
{
}
