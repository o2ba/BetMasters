package service.app.user.account.modify;

import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;

/**
 * This interface provides a contract for modifying user account details.
 */
public interface ModifyAccountService {

    /**
     * Deletes a user account based on the provided user ID.
     *
     * @param uid the unique identifier of the user account to be deleted
     * @throws UnhandledErrorException if an unexpected error occurs during the process
     * @throws UserNotFoundException if no user is found with the provided ID
     */
    void deleteAccount(int uid) throws UnhandledErrorException, UserNotFoundException;
}
