package controller.old.fixture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import common.exception.InternalServerError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.fixture.FixtureService;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.common.model.Fixture;


import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "Fixtures and Odds")
public class FixtureBetsController {

    private final FixtureService fixtureService;

    public FixtureBetsController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @ApiOperation(value = "Get bet types", notes = "This endpoint allows you to get bet types.", tags = "Fixtures and Odds")
    @GetMapping("/football/v2/odds/types")
    public ResponseEntity<String> getBetTypes() {
        try {
            return ResponseEntity.status(200).body(fixtureService.getBetTypes());
        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body("No bet types found");
        }
    }

    @ApiOperation(value = "Get odds for a specific fixture", notes = "This endpoint allows you to get odds for a specific fixture given an ID.", tags = "Fixtures and Odds")
    @GetMapping("/football/v2/odds/for-fixture")
    public ResponseEntity<String> getOddsForFixture(
            @ApiParam(value = "Fixture ID", required = true) @RequestParam("fixtureID") int fixtureID,
            @ApiParam(value = "Odd ID", required = true) @RequestParam("oddID") int oddID
    ) throws Exception {
        try {
            Map<String, Double> odds = fixtureService.getOddsForFixture(fixtureID, oddID);

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String prettyJson = gson.toJson(odds);

            return ResponseEntity.status(200).body(prettyJson);

        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body("No odds found");
        }
    }

    @ApiOperation(value = "Get odds for a a given league and season", notes = "This endpoint allows you to get odds for a given league and season.", tags = "Fixtures and Odds")
    @GetMapping("/football/v2/odds/for-league-and-season")
    public ResponseEntity<String> getOddsForLeagueAndSeason(
            @ApiParam(value = "League ID", required = true) @RequestParam("leagueID") int leagueID,
            @ApiParam(value = "Season ID", required = true) @RequestParam("seasonID") int seasonID,
            @ApiParam(value = "Odd ID", required = true) @RequestParam("oddID") int oddID
    ) throws Exception {
        try {
            Map<Integer, Map<String, Double>> odds = fixtureService.getOddsForLeagueAndSeason(leagueID, seasonID, oddID);

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String prettyJson = gson.toJson(odds);

            return ResponseEntity.status(200).body(prettyJson);

        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body("No odds found");
        }
    }

    @ApiOperation(value = "Get fixture by ID", notes = "This endpoint allows you to get a fixture by its ID.", tags = "Fixtures and Odds")
    @GetMapping("/football/v2/fixtures/by-id")
    public ResponseEntity<String> getFixturesById(
            @ApiParam(value = "Fixture ID", required = true) @RequestParam("fixtureID") int fixtureID
    ) {
        try {
            Fixture fixture = fixtureService.getFixtureByID(fixtureID);
            return ResponseEntity.status(200).body(fixture.toJson());
        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body("Fixture not found");
        } catch (FootballApiException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "Get fixtures by league and season", notes = "This endpoint allows you to get fixtures by league and season.", tags = "Fixtures and Odds")
    @GetMapping("/football/v2/fixtures/by-league-and-season")
    public ResponseEntity<String> getFixturesByLeagueAndSeason(
            @ApiParam(value = "League ID", required = true) @RequestParam("leagueID") int leagueID,
            @ApiParam(value = "Season ID", required = true) @RequestParam("seasonID") int seasonID
    ) {
        try {

            List<Fixture> fixtures = fixtureService.getFixturesByLeagueAndSeason(leagueID, seasonID);

            JsonArray jsonArray = new JsonArray();

            for (Fixture fixture : fixtures) {
                jsonArray.add(fixture.toJsonElement());
            }

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String prettyJson = gson.toJson(jsonArray);

            if (fixtures.isEmpty()) {
                return ResponseEntity.status(404).body("No fixtures found");
            } else {
                return ResponseEntity.status(200).body(prettyJson);
            }

        } catch (FootballApiException e) {
            throw new RuntimeException(e);
        } catch (InternalServerError e) {
            return ResponseEntity.status(500).body("No fixtures found");
        }
    }

}