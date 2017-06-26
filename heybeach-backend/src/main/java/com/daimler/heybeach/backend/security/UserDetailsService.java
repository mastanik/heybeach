package com.daimler.heybeach.backend.security;

import com.daimler.heybeach.backend.dao.UserDao;
import com.daimler.heybeach.backend.entities.Role;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.RoleException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userDao.findByUsername(username);
            if (user != null) {
                List<Role> roles = roleService.getRoles(user);
                List<String> roleNames = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
                List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(roleNames.toArray(new String[roleNames.size()]));
                String password = user.getPassword();
                return new LoggedInUser(user.getId(), username, password, auth);
            }
        } catch (DaoException | RoleException | NotFoundException | ValidationException e) {
            logger.error("Exception occurred while authorizing a user", e);
        }
        return null;
    }
}
