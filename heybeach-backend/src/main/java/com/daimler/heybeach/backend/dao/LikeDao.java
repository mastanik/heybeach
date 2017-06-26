package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.Like;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDao extends GenericDao<Like, Like> {

    public Long countByPictureId(Long pictureId) throws DaoException, ValidationException {
        Condition.ConditionBuilder builder = new Condition.ConditionBuilder();
        builder.fieldName("picture_id")
                .comparator(Condition.EQ())
                .value(pictureId);
        return count(builder.build());
    }
}
