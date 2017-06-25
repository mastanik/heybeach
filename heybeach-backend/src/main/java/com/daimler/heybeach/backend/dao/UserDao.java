package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.data.core.Condition;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericDao<Long, User> {

    public User findByUsername(String username) throws DaoException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("username")
                .comparator(Condition.EQ())
                .value(username);
        return findAllWith(builder.build()).iterator().next();
    }
}
