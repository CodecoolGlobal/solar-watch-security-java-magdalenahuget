package com.company.solarwatch.service;

import com.company.solarwatch.model.Role;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.repository.UserEntityRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public UserService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public UserEntity findCurrentUser() {
        UserDetails contextUser = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String username = contextUser.getUsername();
        return userEntityRepository.findUserByName(username)
                .orElseThrow(() -> new IllegalArgumentException(format("could not find user %s in the repository", username)));

    }

    public void addRoleFor(UserEntity user, Role role) {

        Set<Role> oldRoles = user.roles();

        Set<Role> copiedRoles = new HashSet<>(oldRoles);
        copiedRoles.add(role);

        userEntityRepository.upsertUser(
                new UserEntity(user.username(), user.password(), Set.copyOf(copiedRoles)));
    }
}
