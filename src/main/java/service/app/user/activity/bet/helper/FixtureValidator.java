package service.app.user.activity.bet.helper;

import service.app.user.activity.bet.exception.InvalidInputException;

/**
 * The FixtureValidator interface provides a contract for validating fixtures in the betting system.
 * Implementations of this interface are expected to provide a concrete implementation of the bettingOpenForFixture method.
 *
 * <p>This interface is a part of the service layer in the application architecture,
 * and it interacts with the database layer to check if betting is open for a given fixture.</p>
 *
 * <p>Exception handling is a responsibility of the implementations,
 * and they are expected to handle exceptions related to invalid input.</p>
 *
 */
public interface FixtureValidator {

    /**
     * Checks if betting is open for a given fixture.
     *
     * <p>This method interacts with the database to check if betting is open for a given fixture.
     * It is expected to handle exceptions related to invalid input.</p>
     *
     * @param fixtureId the ID of the fixture to check
     * @return true if betting is open for the fixture, false otherwise
     * @throws InvalidInputException if the fixtureId is invalid
     */
    boolean bettingOpenForFixture(int fixtureId) throws InvalidInputException;

}
