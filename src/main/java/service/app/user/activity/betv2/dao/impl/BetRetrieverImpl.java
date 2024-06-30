package service.app.user.activity.betv2.dao.impl;

import common.exception.gen.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import service.app.user.activity.betv2.dao.inferfaces.BetRetriever;
import service.general.external.dbRequest.DbRequest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class BetRetrieverImpl implements BetRetriever {

    private static final String RETRIEVE_BETS =
            "SELECT * FROM bets WHERE uid = ? ORDER BY timestamp DESC";


    DbRequest dbRequest;

    @Autowired
    public BetRetrieverImpl(DbRequest dbRequest) {
        this.dbRequest = dbRequest;
    }

    @Override
    public List<Map<String, Object>> retrieveBets(int uid) throws SQLException {

        return dbRequest.query(
                RETRIEVE_BETS,
                uid);

    }
}