package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiParam;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.app.fixtureService.v2.GetBetTypesService;
import service.app.fixtureService.v2.GetOddsService;
import service.app.fixtureService.v2.dto.FootballResponse;
import service.app.fixtureService.v2.dto.GetOdds;

import java.util.List;
import java.util.Map;

@Controller
public class FixtureBetsControllerV2 {

    GetBetTypesService getBetTypes;
    GetOddsService getOddsService;

    public FixtureBetsControllerV2(GetBetTypesService getBetTypes, GetOddsService getOddsService) {
        this.getBetTypes = getBetTypes;
        this.getOddsService = getOddsService;
    }

    @GetMapping("/v2/getOddsForFixture")
    public ResponseEntity<String> getOddsForFixture(
            @ApiParam(value = "Fixture ID", required = true) @RequestParam("fixtureID") int fixtureID,
            @ApiParam(value = "Odd ID", required = true) @RequestParam("oddID") int oddID
    ) {
        try {
            List<Map<String, Double>> footballResponse = getOddsService.getOdds(String.valueOf(fixtureID), oddID);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(footballResponse);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while getting odds for fixture" + e.getMessage());
        }
    }

    @GetMapping("/v2/getBetTypes")
    public ResponseEntity<String> getBetTypes() {
        try {
            return ResponseEntity.ok(getBetTypes.getBetTypesAsJsonArray());
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body("Error while getting bet types");
        }
    }
}
