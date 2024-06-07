package common.exception.fixture;


/*
 * Exception for when there are no fixtures returned from the database.
 */
public class NoFixturesException extends Exception {
    public NoFixturesException() {
        super("No fixtures found");
    }

    public NoFixturesException(String message) {
        super(message);
    }
}
