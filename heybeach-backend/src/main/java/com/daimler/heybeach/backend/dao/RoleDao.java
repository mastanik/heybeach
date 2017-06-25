package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Role;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.data.core.Condition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDao extends GenericDao<Long, Role> {

    public Role findByName(String name) throws DaoException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("name")
                .comparator(Condition.EQ())
                .value(name);
        return findAllWith(builder.build()).iterator().next();
    }

    public List<Role> findHierarchy(Integer priority) throws DaoException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("priority")
                .comparator(Condition.LT())
                .value(priority);
        return findAllWith(builder.build());
    }
}
