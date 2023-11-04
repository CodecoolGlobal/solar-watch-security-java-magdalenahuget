package com.company.solarwatch.service;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void createRole(Role role) {
     roleRepository.save(role);
    }

    public Role findByName(RoleType roleTypeName){
        return roleRepository.findByName(roleTypeName);
    }
}

