package service.app.user.activity.bet.helper;

import common.exception.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.fixture.FixtureService;
import service.app.fixture.common.exception.FootballApiException;
import service.app.fixture.odds.BetTypes;
import service.app.user.activity.bet.exception.InvalidInputException;
import service.app.user.activity.bet.exception.NoOddsForGameException;

import java.util.Map;

@Component
public class BetHelperImpl implements BetHelper {

    FixtureService fixtureService;

    @Autowired
    public BetHelperImpl(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @Override
    public double getOddsMultiplier(int fixtureId, String betType, String prediction)
            throws NoOddsForGameException, InvalidInputException {

        Map<String, Double> odds;

        try {
            odds = fixtureService.getOddsForFixture(fixtureId, getBetTypeFromShortName(betType).getId());
        } catch (FootballApiException | InternalServerError e) {
            throw new NoOddsForGameException("Error while getting odds for fixture " + e);
        }

        if (odds == null) throw new NoOddsForGameException("No odds available for the game");

        if (!odds.containsKey(prediction)) throw new InvalidInputException(InvalidInputException.Type.INVALID_PREDICTION);

        return odds.get(prediction);
    }

    private BetTypes getBetTypeFromShortName(String shortName) throws InvalidInputException {
        for (BetTypes betType : BetTypes.values()) {
            if (betType.getShortName().equals(shortName)) {
                return betType;
            }
        }
        throw new InvalidInputException(InvalidInputException.Type.INVALID_BET);
    }

}