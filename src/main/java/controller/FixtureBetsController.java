package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import common.model.football.fixture.FixtureObject;
import service.app.fixture.FixtureService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "FixtureBetsController", description = "FixtureBetsController")
@Controller
public class FixtureBetsController {
    @ApiOperation(value = "Get fixtures with odds", notes = "This endpoint allows you to get fixtures with odds.")
    @GetMapping("/fixturesWithOdds")
    @ResponseBody
    public ResponseEntity<String> fixturesWithOdds(
            @ApiParam(value = "bookmaker") @RequestParam String bookmaker,
            @ApiParam(value = "league") String league,
            @ApiParam(value = "season") String season,
            @ApiParam(value = "fixture_id") String fixture_id,
            @ApiParam(value = "Timezone") @RequestParam(defaultValue = "Europe/Berlin")
            String timezone,
            @ApiParam(value = "Games in the future only") @RequestParam(defaultValue = "false")
            boolean future_games_only,
            @ApiParam(value = "Only include games with bets available") @RequestParam(defaultValue = "false")
            boolean games_with_bets_only) {

        Map<String, String> params = new HashMap<>();
        if (bookmaker != null) params.put("bookmaker", bookmaker);
        if (league != null) params.put("league", league);
        if (season != null) params.put("season", season);
        if (fixture_id != null) params.put("id", fixture_id);
        if (timezone != null) params.put("timezone", timezone);

        String dateToday =  java.time.LocalDate.now()
                .minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateIn14Days = java.time.LocalDate.now()
                .plusDays(14).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (future_games_only) params.put("from", dateToday);
        if (future_games_only) params.put("to", dateIn14Days);

        FixtureService f;
        List<FixtureObject> fixtureObjects;
        JsonArray fixturesWithOdds;

        try {
            f = new FixtureService(params, future_games_only);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Request." +
                    "Please refer to the documentation for the required parameter combinations.");
        }

        try {
            fixtureObjects = f.getFixturesWithOdds(games_with_bets_only);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error with API request " + e.getMessage());
        }

        try {
            fixturesWithOdds = f.toJsonFixtures(fixtureObjects);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error with JSON Conversion");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(fixturesWithOdds);

        return ResponseEntity.ok(prettyJson);
    }
}