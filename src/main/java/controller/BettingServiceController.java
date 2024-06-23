package controller;

import io.swagger.annotations.ApiParam;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.app.authRequestService.simpleBettingService.BettingService;
import service.app.fixtureService.v2.GetBetTypesService;

@Controller
public class BettingServiceController {

    BettingService bettingService;
    GetBetTypesService getBetTypes;

    @Autowired
    public BettingServiceController(BettingService bettingService, GetBetTypesService getBetTypes) {
        this.bettingService = bettingService;
        this.getBetTypes = getBetTypes;
    }

    @GetMapping("/getBetTypes")
    public ResponseEntity<String> getBetTypes() {
        try {
            return ResponseEntity.ok(getBetTypes.getBetTypesAsJsonArray());
        } catch (JSONException e) {
            return ResponseEntity.badRequest().body("Error while getting bet types");
        }
    }

    @PostMapping("/placeBet")
    public ResponseEntity<String> placeBet(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The fixture ID", required = true) @RequestParam int fixtureID,
            @ApiParam(value = "The type of bet", required = true) @RequestParam String betType,
            @ApiParam(value = "What the user is betting on (i.e. 'Home', 'Draw', 'Away')", required = true) @RequestParam String selectedBet,
            @ApiParam(value = "How much the user is betting", required = true) @RequestParam double amount) {
        bettingService.placeBet(jwtToken, email, uid, fixtureID, betType, selectedBet, amount);
        return ResponseEntity.ok("Bet placed successfully");
    }

}
