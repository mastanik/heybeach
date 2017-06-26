package com.daimler.heybeach.backend.transaction;

import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ServiceException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class TransactionalOperation {
    public void executeInTransaction(DataSource dataSource) throws ServiceException, DaoException {
        try (Connection con = dataSource.getConnection()) {
            try {
                con.setAutoCommit(false);
                execute(con);
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public abstract void execute(Connection con) throws ServiceException, DaoException;

}
