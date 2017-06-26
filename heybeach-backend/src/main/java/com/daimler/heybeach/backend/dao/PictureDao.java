package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Picture;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PictureDao extends GenericDao<Long, Picture> {
    public Picture findByIdAndUserId(Long id, Long userId) throws DaoException, EntityNotFoundException, ValidationException {
        Condition idCondition = new Condition.ConditionBuilder()
                .fieldName("id")
                .comparator(Condition.EQ())
                .value(id).build();
        Condition userIdIdCondition = new Condition.ConditionBuilder()
                .fieldName("user_id")
                .comparator(Condition.EQ())
                .value(userId).build();
        List<Picture> pictures = findAllWith(idCondition, userIdIdCondition);
        if (pictures.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return pictures.iterator().next();
    }

}
