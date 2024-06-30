package service.app.fixture.fixture;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.fixture.common.model.Fixture;
import service.app.fixture.common.model.FootballResponse;
import service.app.fixture.fixture.GetFixtureService;
import service.app.fixture.fixture.request.GetFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetFixtureServiceImpl implements GetFixtureService {

    GetFixture getFixture;

    @Autowired
    public GetFixtureServiceImpl(GetFixture getFixture) {
        this.getFixture = getFixture;
    }

    @Override
    public Fixture getFixtureById(int fixture) throws InternalServerError, FixtureNotFoundException {
        try {
            FootballResponse footballResponse = getFixture.getFixtureById(fixture);
            if (footballResponse.results() == 0) throw new FixtureNotFoundException("Fixture not found");
            JsonElement f =  footballResponse.response().get(0);
            return toFixtureObject(f);
        } catch (Exception e) {
            throw new InternalServerError("Error while getting fixture");
        }
    }

    @Override
    public List<Fixture> getFixturesByLeagueAndSeason(int leagueId, int season) throws InternalServerError {
        try {
            FootballResponse footballResponse = getFixture.getFixturesByLeagueAndSeason(leagueId, season);

            if (footballResponse.results() == 0) throw new FixtureNotFoundException("No fixtures found for this league and season");
            List<Fixture> fixtures = new ArrayList<>();
            for (JsonElement f : footballResponse.response().getAsJsonArray()) {
                fixtures.add(toFixtureObject(f));
            }
            return fixtures;
        } catch (Exception e) {
            throw new InternalServerError("Error while getting fixtures");
        }
    }

    private Fixture toFixtureObject(JsonElement f) throws InternalServerError {

        JsonObject fixtureObj = f.getAsJsonObject().get("fixture").getAsJsonObject();
        JsonObject leagueObj = f.getAsJsonObject().get("league").getAsJsonObject();
        JsonObject teamsObj = f.getAsJsonObject().get("teams").getAsJsonObject();
        JsonObject goalsObj = f.getAsJsonObject().get("goals").getAsJsonObject();
        JsonObject scoreObj = f.getAsJsonObject().get("score").getAsJsonObject();
        JsonObject penaltyObj = scoreObj.has("penalty") && !scoreObj.get("penalty").isJsonNull() ? scoreObj.getAsJsonObject("penalty") : null;

        try {
            return new Fixture(
                    Optional.ofNullable(leagueObj.get("name")).map(JsonElement::getAsString).orElse(null),
                    leagueObj.has("id") && !leagueObj.get("id").isJsonNull() ? leagueObj.get("id").getAsInt() : -1,
                    fixtureObj.has("id") && !fixtureObj.get("id").isJsonNull() ? fixtureObj.get("id").getAsInt() : -1,
                    Optional.ofNullable(fixtureObj.get("date")).map(JsonElement::getAsString).orElse(null),
                    Optional.ofNullable(teamsObj.getAsJsonObject("home"))
                            .map(o -> o.getAsJsonObject().get("name"))
                            .map(JsonElement::getAsString)
                            .orElse(null),
                    Optional.ofNullable(teamsObj.getAsJsonObject("home"))
                            .map(o -> o.getAsJsonObject().get("logo"))
                            .map(JsonElement::getAsString)
                            .orElse(null),
                    Optional.ofNullable(teamsObj.getAsJsonObject("away"))
                            .map(o -> o.getAsJsonObject().get("name"))
                            .map(JsonElement::getAsString)
                            .orElse(null),
                    Optional.ofNullable(teamsObj.getAsJsonObject("away"))
                            .map(o -> o.getAsJsonObject().get("logo"))
                            .map(JsonElement::getAsString)
                            .orElse(null),
                    Optional.ofNullable(fixtureObj.getAsJsonObject("status"))
                            .map(o -> o.getAsJsonObject().get("short"))
                            .map(JsonElement::getAsString)
                            .orElse(null),
                    goalsObj.has("home") && !goalsObj.get("home").isJsonNull() ? goalsObj.get("home").getAsInt() : 0,
                    goalsObj.has("away") && !goalsObj.get("away").isJsonNull() ? goalsObj.get("away").getAsInt() : 0,
                    penaltyObj != null && penaltyObj.has("home") && !penaltyObj.get("home").isJsonNull() ? penaltyObj.get("home").getAsInt() : -1,
                    penaltyObj != null && penaltyObj.has("away") && !penaltyObj.get("away").isJsonNull() ? penaltyObj.get("away").getAsInt() : -1,
                    fixtureObj.getAsJsonObject("status").has("elapsed") && !fixtureObj.getAsJsonObject("status").get("elapsed").isJsonNull() ? fixtureObj.getAsJsonObject("status").get("elapsed").getAsInt() : -1
            );

        } catch (Exception e) {
            return null;
        }
    }
}
