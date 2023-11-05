package com.company.solarwatch.security.service;

import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username = " + username);
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(username);
        System.out.println("userEntity = " + userEntity);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Role role : userEntity.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.getName().name()));
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), roles);
    }
}
