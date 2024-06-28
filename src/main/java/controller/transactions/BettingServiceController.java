package controller.transactions;

import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.register.ValidationException;
import common.exception.transactions.NotEnoughBalanceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.app.authRequestService.simpleBettingService.BettingService;
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
            bettingService.placeBet(jwtToken, email, uid, fixtureID, betType, selectedBet, amount);
        } catch (InternalServerError e) {
            return ResponseEntity.badRequest().body("Internal server error");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.badRequest().body("Not authorized");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body("Validation error");
        } catch (NotEnoughBalanceException e) {
            return ResponseEntity.badRequest().body("Not enough balance");
        }

        return ResponseEntity.ok("Bet placed successfully");
    }

    @PostMapping("/betting/cancel")
    @ApiOperation(value = "Cancel a bet", tags = "Betting")
    public ResponseEntity<String> cancelBet(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid,
            @ApiParam(value = "The bet ID", required = true) @RequestParam int betID) {
        return ResponseEntity.ok("Bet cancelled successfully");
    }

    @GetMapping("/betting/get-bets-for-user")
    @ApiOperation(value = "Get bet types", notes = "This endpoint allows you to get bet types.", tags = "Betting")
    public ResponseEntity<String> getBetsForUser(
            @ApiParam(value = "The JWT token of the user", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The email of the user", required = true) @RequestParam String email,
            @ApiParam(value = "The user ID", required = true) @RequestParam int uid) {
        return ResponseEntity.ok("Bets for user");
    }

}
