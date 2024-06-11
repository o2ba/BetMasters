package controller;

import common.exception.InternalServerError;
import common.model.football.fixture.Fixture;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.app.footballService.fixtureOdds.exception.InvalidResponseException;
import service.app.footballService.fixtureOdds.fixture.GetFixturesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NewFixtureController {

    GetFixturesService getFixturesService;

    @Autowired
    public NewFixtureController(GetFixturesService getFixturesService) {
        this.getFixturesService = getFixturesService;
    }

    @ApiOperation(value = "Get fixtures by league", notes = "This endpoint allows you to get fixtures by league.")
    @GetMapping("/getFixturesByLeague")
    public ResponseEntity<String> getFixtures(
            @ApiParam(value = "league") String league,
            @ApiParam(value = "season") String season,
            @ApiParam(value = "Timezone") @RequestParam(defaultValue = "Europe/Berlin")
            String timezone
    ) {

        Map<String, String> params = new HashMap<>();
        if (league != null) params.put("league", league);
        if (season != null) params.put("season", season);
        if (timezone != null) params.put("timezone", timezone);

        try {
            List<Fixture> fixtures = getFixturesService.getFixtures(params);


            StringBuilder output = new StringBuilder();

            for (Fixture fixture : fixtures) {
                output.append(fixture.toString()).append("\n");
            }

            return ResponseEntity.ok(output.toString());

        } catch (InvalidResponseException | InternalServerError e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting fixtures");
        }

    }

}
