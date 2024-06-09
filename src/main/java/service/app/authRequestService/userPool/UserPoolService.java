package service.app.authRequestService.userPool;

import service.app.authRequestService.userPool.model.UserPool;

public interface UserPoolService {

    /**
     * Create a new user pool with the given name, locked status, client secret, and owner UID.
     *
     * @param poolName the name of the user pool
     * @param locked whether the user pool is locked
     * @param clientSecret the client secret for the user pool
     */
    UserPool createUserPool(String poolName, boolean locked, String clientSecret, int ownerUid);


    void deleteUserPool(String poolId);
    void updateUserPool(String poolId, String clientId, String clientSecret);
    void getUserPool(String poolId);
    void listUserPools();
    void addUserToPool(String poolId, String userId);
    void removeUserFromPool(String poolId, String userId);
    void listUsersInPool(String poolId);
    void listPoolsForUser(String userId);
    void listUsersInAllPools();
    void listPoolsForAllUsers();
    void listAll();
}
