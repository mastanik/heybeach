package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Payment;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentDao extends GenericDao<Long, Payment> {

    public Payment findSuccessfullByOrderId(Long orderId) throws DaoException, EntityNotFoundException, ValidationException {
        Condition orderIdCondition = new Condition.ConditionBuilder()
                .fieldName("order_id")
                .comparator(Condition.EQ())
                .value(orderId).build();
        Condition userIdIdCondition = new Condition.ConditionBuilder()
                .fieldName("result")
                .comparator(Condition.EQ())
                .value(Boolean.TRUE).build();
        List<Payment> payments = findAllWith(orderIdCondition, userIdIdCondition);
        if (payments.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return payments.iterator().next();
    }

}
