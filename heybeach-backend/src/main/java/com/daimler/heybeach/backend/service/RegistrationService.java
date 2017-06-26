package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.dto.RegistrationDto;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.RegistrationException;
import com.daimler.heybeach.backend.exception.RoleException;
import com.daimler.heybeach.backend.exception.UserException;
import com.daimler.heybeach.backend.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public void register(RegistrationDto dto) throws RegistrationException, ValidationException {
        try {
            User user = new User();
            user.setEmail(dto.getEmail());
            user.setFirstname(dto.getFirstname());
            user.setLastname(dto.getLastname());
            user.setPassword(dto.getPassword());
            user.setUsername(dto.getUsername());

            userService.create(user);
            roleService.grantRoles(user, dto.getRole());
        } catch (RoleException | UserException e) {
            throw new RegistrationException(e.getMessage(), e);
        }
    }

}
