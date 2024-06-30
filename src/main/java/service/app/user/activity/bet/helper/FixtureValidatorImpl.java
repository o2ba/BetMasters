package service.app.user.activity.bet.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.fixture.FixtureService;
import service.app.fixture.common.model.Fixture;
import service.app.user.activity.bet.exception.InvalidInputException;

@Component
public class FixtureValidatorImpl implements FixtureValidator{

    FixtureService fixtureService;

    @Autowired
    public FixtureValidatorImpl(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @Override
    public boolean bettingOpenForFixture(int fixtureId) throws InvalidInputException {
        try {
            Fixture fixture = fixtureService.getFixtureByID(fixtureId);

            return fixture.bettingAllowed();
        } catch (Exception e) {
            throw new InvalidInputException(InvalidInputException.Type.INVALID_FIXTURE);
        }
    }
}
