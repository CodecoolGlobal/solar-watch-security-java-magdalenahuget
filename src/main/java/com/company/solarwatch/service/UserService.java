package com.company.solarwatch.service;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.UserEntityRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


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
        return userEntityRepository.findUserEntityByUsername(username);
    }

    public void addRoleFor(UserEntity user, RoleType roleType) {

        Set<Role> oldRoleTypes = user.getRoles();
        Set<Role> copiedRoleTypes = new HashSet<>(oldRoleTypes);
        copiedRoleTypes.add(new Role(roleType));
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(user.getUsername());
        userEntity.setRoles(copiedRoleTypes);
        userEntityRepository.save(userEntity);
    }
}
