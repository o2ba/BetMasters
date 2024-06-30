package service.app.user.account.modify.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;

@Component
public class DeleteAccountDaoImpl implements DeleteAccountDao {

    private final String DELETE_ACCOUNT = "DELETE FROM users WHERE uid = ?";

    DbRequest dbRequest;

    @Autowired
    public DeleteAccountDaoImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public int deleteAccount(int uid) throws SQLException {
        return dbRequest.execute(DELETE_ACCOUNT, uid);
    }
}
