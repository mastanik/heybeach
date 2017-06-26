package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Order;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao extends GenericDao<Long, Order> {

    public Order findByIdAndUserId(Long id, Long userId) throws DaoException, EntityNotFoundException, ValidationException {
        Condition idCondition = new Condition.ConditionBuilder()
                .fieldName("id")
                .comparator(Condition.EQ())
                .value(id).build();
        Condition userIdIdCondition = new Condition.ConditionBuilder()
                .fieldName("user_id")
                .comparator(Condition.EQ())
                .value(userId).build();
        List<Order> orders = findAllWith(idCondition, userIdIdCondition);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return orders.iterator().next();
    }
}
