package com.company.solarwatch.repository;

import com.company.solarwatch.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findUserEntityByUsername(String userName);
}
