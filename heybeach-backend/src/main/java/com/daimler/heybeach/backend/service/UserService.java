package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.UserDao;
import com.daimler.heybeach.backend.dao.UserRoleDao;
import com.daimler.heybeach.backend.dto.UserDto;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.entities.UserRole;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.UserException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    public User findById(Long id) throws UserException, NotFoundException, ValidationException {
        try {
            return userDao.findById(id);
        } catch (DaoException e) {
            throw new UserException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested user not found");
        }
    }

    public List<User> findAll() throws UserException, ValidationException {
        try {
            return userDao.findAll();
        } catch (DaoException e) {
            throw new UserException(e.getMessage(), e);
        }
    }

    public void create(User user) throws UserException, ValidationException {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userDao.save(user);
        } catch (DaoException e) {
            throw new UserException(e.getMessage(), e);
        }
    }

    public void update(Long id, UserDto userDto) throws UserException, NotFoundException, ValidationException {
        try {
            User user = userDao.findById(id);
            user.setEmail(userDto.getEmail());
            user.setFirstname(userDto.getFirstname());
            user.setLastname(userDto.getLastname());
            user.setUsername(userDto.getUsername());

            userDao.save(user);
        } catch (DaoException e) {
            throw new UserException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested user not found");
        }
    }

    public void remove(Long id) throws UserException, NotFoundException, ValidationException {
        try {
            User user = userDao.findById(id);
            List<UserRole> userRoles = userRoleDao.findAllWith(new Condition.ConditionBuilder()
                    .fieldName("user_id")
                    .comparator(Condition.EQ())
                    .value(id).build());
            for (UserRole userRole : userRoles) {
                userRoleDao.remove(userRole);
            }

            userDao.remove(user);
        } catch (DaoException e) {
            throw new UserException(e.getMessage(), e);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Requested user not found");
        }
    }
}
