package controller.user.bet;

import com.google.gson.GsonBuilder;
import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.app.fixture.common.exception.FixtureNotFoundException;
import service.app.user.activity.betv2.BettingServiceV2;
import service.app.user.activity.betv2.exception.BettingNotOpenException;
import service.app.user.activity.betv2.exception.InvalidInputException;
import service.app.user.activity.betv2.exception.NoOddsForGameException;
import service.app.user.activity.transact.exception.InvalidUserException;
import service.app.user.activity.transact.exception.NotEnoughBalanceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BetControllerV2 {

    BettingServiceV2 bettingServiceV2;


    @Autowired
    public BetControllerV2(BettingServiceV2 bettingServiceV2) {
        this.bettingServiceV2 = bettingServiceV2;
    }

    @PostMapping("betting/v2/place")
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
            InvalidUserException {

        int bet = 0;

        bet = bettingServiceV2.placeBet(uid, amount, fixtureId, type, prediction);

        Map<String, Object> response = new HashMap<>();
        response.put("bet_id", bet);
        response.put("message", "Bet placed successfully");

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }


    @GetMapping("betting/v2/get-bets")
    public ResponseEntity<String> getBets(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid)
            throws UserNotFoundException, UnhandledErrorException {

        List<Map<String, Object>> r = bettingServiceV2.getBets(uid);

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(r);

        return ResponseEntity.ok(json);
    }

    @PutMapping("betting/v2/claim")
    public ResponseEntity<String> claimBets(
            @ApiParam(value = "The user's JWT token", required = true) @RequestParam String jwtToken,
            @ApiParam(value = "The user's email", required = true) @RequestParam String email,
            @ApiParam(value = "The user's UID", required = true) @RequestParam int uid)
            throws UserNotFoundException, UnhandledErrorException {

        int totalClaimed = bettingServiceV2.claimBets(uid);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bets claimed successfully");
        response.put("total_claimed", totalClaimed);

        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        String json = gsonBuilder.create().toJson(response);

        return ResponseEntity.ok(json);
    }


}
