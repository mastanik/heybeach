package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Hashtag;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HashtagDao extends GenericDao<Long, Hashtag> {

    public Hashtag findByName(String name) throws DaoException, ValidationException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("name")
                .comparator(Condition.EQ())
                .value(name);
        List<Hashtag> list = findAllWith(builder.build());
        if (list.size() > 0) {
            return list.iterator().next();
        }
        return null;
    }
}
