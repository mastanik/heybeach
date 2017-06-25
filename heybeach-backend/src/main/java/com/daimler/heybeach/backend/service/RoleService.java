package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dao.RoleDao;
import com.daimler.heybeach.backend.dao.UserRoleDao;
import com.daimler.heybeach.backend.entities.Role;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.entities.UserRole;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.RoleException;
import com.daimler.heybeach.data.core.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    public void create(Role role) throws RoleException {
        try {
            roleDao.save(role);
            role.setName(role.getName());
        } catch (DaoException e) {
            throw new RoleException(e.getMessage(), e);
        }
    }

    public void remove(Long id) throws RoleException {
        try {
            Role role = roleDao.findById(id);
            List<UserRole> userRoles = userRoleDao.findAllWith(new Condition.ConditionBuilder()
                    .fieldName("role_id")
                    .comparator(Condition.EQ())
                    .value(id).build());
            for (UserRole userRole : userRoles) {
                userRoleDao.remove(userRole);
            }
            roleDao.remove(role);
        } catch (DaoException e) {
            throw new RoleException(e.getMessage(), e);
        }
    }

    public void grantRoles(User user, String roleName) throws RoleException {
        try {
            Role role = roleDao.findByName(roleName.toUpperCase());
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleDao.save(userRole);
        } catch (DaoException e) {
            throw new RoleException(e.getMessage(), e);
        }
    }

    public void grantRoleHierarchy(User user, String roleName) throws RoleException {
        try {
            Role role = roleDao.findByName(roleName.toUpperCase());
            List<Role> roles = roleDao.findHierarchy(role.getPriority());
            roles.add(role);

            for (Role roleToGrant : roles) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleToGrant.getId());
                userRoleDao.save(userRole);
            }
        } catch (DaoException e) {
            throw new RoleException(e.getMessage(), e);
        }
    }

    public List<Role> getRoles(User user) throws RoleException {
        try {
            List<UserRole> userRoles = userRoleDao.findAllWith(new Condition.ConditionBuilder()
                    .fieldName("user_id")
                    .comparator(Condition.EQ())
                    .value(user.getId()).build());
            List<Role> roles = new LinkedList<>();
            for (UserRole userRole : userRoles) {
                roles.add(roleDao.findById(userRole.getRoleId()));
            }
            return roles;
        } catch (DaoException e) {
            throw new RoleException(e.getMessage(), e);
        }
    }

}
