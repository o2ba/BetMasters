package controller.user.bet;

import com.google.gson.GsonBuilder;
import common.exception.InternalServerError;
import common.exception.NotAuthorizedException;
import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.user.activity.bet.BettingService;
import service.app.user.activity.bet.exception.BettingNotOpenException;
import service.app.user.activity.bet.exception.InvalidInputException;
import service.app.user.activity.bet.exception.NoOddsForGameException;
import service.app.user.activity.bet.exception.StatusAlreadyIdentical;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;
import service.general.internal.authService.AuthorizationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "Betting")
public class BetController {

    BettingService bettingService;
    AuthorizationService authorizationService;


    @Autowired
    public BetController(BettingService bettingService, AuthorizationService authorizationService) {
        this.bettingService = bettingService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("betting/v2/place")
    @ApiOperation(value = "Place a bet", notes = "This endpoint allows you to place a bet.", tags = "Betting")
    public ResponseEntity<String> placeBet(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "Fixture id", required = true) @RequestParam int fixtureId,
            @ApiParam(value = "The bet amount", required = true) @RequestParam double amount,
            @ApiParam(value = "The bet type", required = true) @RequestParam String type,
            @ApiParam(value = "The prediction", required = true) @RequestParam String prediction)
            throws UserNotFoundException,
            BettingNotOpenException,
            FixtureNotFoundException,
            UnhandledErrorException,
            NoOddsForGameException,
            NotEnoughBalanceException,
            InvalidInputException,
            InvalidUserException, NotAuthorizedException, InternalServerError {

        authorizationService.authorizeRequest(jwtToken, uid, email);

        int bet = 0;

        bet = bettingService.placeBet(uid, amount, fixtureId, type, prediction);

        Map<String, Object> response = new HashMap<>();
        response.put("bet_id", bet);
        response.put("message", "Bet placed successfully");

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }


    @GetMapping("betting/v2/get-bets")
    @ApiOperation(value = "Get bets", notes = "This endpoint allows you to get bets.", tags = "Betting")
    public ResponseEntity<String> getBets(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid)
            throws UserNotFoundException, UnhandledErrorException, NotAuthorizedException, InternalServerError {

        authorizationService.authorizeRequest(jwtToken, uid, email);

        List<Map<String, Object>> r = bettingService.getBets(uid);

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(r);

        return ResponseEntity.ok(json);
    }

    @PutMapping("betting/v2/claim")
    @ApiOperation(value = "Claim bets", notes = "This endpoint allows you to claim bets.", tags = "Betting")
    public ResponseEntity<String> claimBets(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid)
            throws UserNotFoundException, UnhandledErrorException, StatusAlreadyIdentical, NotAuthorizedException, InternalServerError {

        authorizationService.authorizeRequest(jwtToken, uid, email);

        int totalClaimed = bettingService.claimBets(uid);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bets claimed successfully");
        response.put("total_claimed", totalClaimed);

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }

    @PatchMapping("betting/v2/cancel")
    @ApiOperation(value = "Cancel a bet", tags = "Betting")
    public ResponseEntity<String> cancelBet(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid,
            @ApiParam(value = "The bet ID", required = true) @RequestParam int betId)
            throws UserNotFoundException, UnhandledErrorException, InvalidInputException, StatusAlreadyIdentical, NotAuthorizedException, InternalServerError {

        authorizationService.authorizeRequest(jwtToken, uid, email);

        bettingService.cancelBet(betId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bet cancelled successfully");

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }

}
