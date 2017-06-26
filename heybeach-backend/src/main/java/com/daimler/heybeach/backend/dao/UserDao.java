package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends GenericDao<Long, User> {

    public User findByUsername(String username) throws DaoException, EntityNotFoundException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("username")
                .comparator(Condition.EQ())
                .value(username);
        List<User> user = findAllWith(builder.build());
        if (user.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return user.iterator().next();
    }
}
