package service.app.user.account.modify;

import common.exception.UnhandledErrorException;
import common.exception.gen.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.app.user.account.modify.dao.DeleteAccountDao;

import java.sql.SQLException;

@Service
public class ModifyAccountServiceImpl implements ModifyAccountService {

    DeleteAccountDao deleteAccountDao;

    @Autowired
    public ModifyAccountServiceImpl(DeleteAccountDao deleteAccountDao) {
        this.deleteAccountDao = deleteAccountDao;
    }

    @Override
    public void deleteAccount(int uid) throws UnhandledErrorException, UserNotFoundException {
        try {
            int r = deleteAccountDao.deleteAccount(uid);
            if (r == 0) {
                throw new UserNotFoundException("User not found");
            }
        } catch (SQLException e) {

            if (e.getSQLState().equals("23503"))
                throw new UserNotFoundException("User not found");

            throw new UnhandledErrorException("Error deleting account");
        }
    }
}
