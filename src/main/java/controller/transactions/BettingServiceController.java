package controller.transactions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.app.userService.betting.BettingService;
import service.app.fixtureService.v2.odds.GetBetTypesService;

@RestController
@Api(tags = "Betting")
public class BettingServiceController {

    BettingService bettingService;
    GetBetTypesService getBetTypes;

    @Autowired
    public BettingServiceController(BettingService bettingService, GetBetTypesService getBetTypes) {
        this.bettingService = bettingService;
        this.getBetTypes = getBetTypes;
    }


    @ApiOperation(value = "Get bet types", notes = "This endpoint allows you to get bet types.", tags = "Betting")
    @PostMapping("/betting/place")
    public ResponseEntity<String> placeBet(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The fixture ID", required = true) @RequestParam int fixtureID,
            @ApiParam(value = "The type of bet", required = true) @RequestParam String betType,
            @ApiParam(value = "What the user is betting on (i.e. 'Home', 'Draw', 'Away')", required = true) @RequestParam String selectedBet,
            @ApiParam(value = "How much the user is betting", required = true) @RequestParam double amount) {

        try {
            int bet = bettingService.placeBet(jwtToken, email, uid, fixtureID, betType, selectedBet, amount);

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            String prettyJson = gsonBuilder.create().toJson(bet);

            return ResponseEntity.ok(prettyJson);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Request Failed: " + e.getMessage());
        }
    }

    @PatchMapping("/betting/cancel")
    @ApiOperation(value = "Cancel a bet", tags = "Betting")
    public ResponseEntity<String> cancelBet(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The bet ID", required = true) @RequestParam int betID) {
        try {
            bettingService.cancelBet(jwtToken, email, uid, betID);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Request Failed: " + e.getMessage());
        }
        return ResponseEntity.ok("Bet cancelled successfully");
    }

    @GetMapping("/betting/get-bets-for-user")
    @ApiOperation(value = "Get bet types", notes = "This endpoint allows you to get bet types.", tags = "Betting")
    public ResponseEntity<String> getBetsForUser(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid) {
        try {
            var r = ResponseEntity.ok(bettingService.getBets(jwtToken, email, uid));

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String prettyJson = gson.toJson(r);

            return ResponseEntity.ok(prettyJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Request Failed: " + e.getMessage());
        }
    }

    @PutMapping("/betting/claim-all")
    @ApiOperation(value = "Claim all bets", tags = "Betting")
    public ResponseEntity<String> claimAllBets(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid) {
        try {
            bettingService.claimBets(jwtToken, email, uid);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Request Failed: " + e.getMessage());
        }
        return ResponseEntity.ok("Bets claimed successfully");
    }

    @GetMapping("/betting/statistics")
    @ApiOperation(value = "Get the total blocked, won and lost amount", tags = "Betting")
    public ResponseEntity<String> getTotalBlockedAmount(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid) {
        try {
            var r = ResponseEntity.ok(bettingService.getStatistics(jwtToken, email, uid));

            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            Gson gson = gsonBuilder.create();
            String prettyJson = gson.toJson(r);

            return ResponseEntity.ok(prettyJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Request Failed: " + e.getMessage());
        }
    }

}
