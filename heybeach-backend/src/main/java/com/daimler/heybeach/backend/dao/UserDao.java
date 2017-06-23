package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.entities.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends GenericDao<Long, User> {
}
