package service.app.user.account.modify.dao;

import java.sql.SQLException;

public interface DeleteAccountDao {
    int deleteAccount(int uid) throws SQLException;
}
