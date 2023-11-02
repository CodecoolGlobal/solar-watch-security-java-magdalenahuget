package com.company.solarwatch.repository;

import com.company.solarwatch.model.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;

@Repository
public class UserEntityRepository {

    private final ConcurrentMap<String, UserEntity> users = new ConcurrentHashMap<>();

    public synchronized Optional<UserEntity> findUserByName(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    public synchronized void createUser(UserEntity user) {
        String userName = user.username();
        if (users.containsKey(userName)) {
            throw new IllegalArgumentException(format("user %s already exists", userName));
        }
        users.put(userName, user);
    }

    public synchronized void upsertUser(UserEntity user) {
        String userName = user.username();
        UserEntity userEntity = findUserByName(userName).orElseThrow();
        UserEntity newUserEntity = new UserEntity(
                userEntity.username(), userEntity.password(), user.roles());
        users.put(userName, newUserEntity);
    }
}
