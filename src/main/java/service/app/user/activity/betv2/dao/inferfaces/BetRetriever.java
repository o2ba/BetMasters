package service.app.user.activity.betv2.dao.inferfaces;

import common.exception.gen.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BetRetriever {
    List<Map<String, Object>> retrieveBets(int uid) throws SQLException, UserNotFoundException;
}
