package com.company.solarwatch.repository;


import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.solarWatchData.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleType roleTypeName);
}
