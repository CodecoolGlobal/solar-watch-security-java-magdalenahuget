package com.company.solarwatch.service;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.payload.JwtResponse;
import com.company.solarwatch.model.payload.UserRequest;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.UserEntityRepository;
import com.company.solarwatch.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserService(UserEntityRepository userEntityRepository, RoleService roleService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userEntityRepository = userEntityRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
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

    public UserEntity createUser(UserRequest signUpRequest) {
        Role role = roleService.findByName(RoleType.ROLE_USER);
        UserEntity user = new UserEntity(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()), Set.of(role));
        UserEntity createdUserEntity = userEntityRepository.save(user);
        System.out.println("User registered = " + user);
        return createdUserEntity;
    }

    public UserEntity createAdmin(UserRequest signUpRequest) {
        Role role = roleService.findByName(RoleType.ROLE_ADMIN);
        UserEntity user = new UserEntity(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()), Set.of(role));
        UserEntity createdUserEntity = userEntityRepository.save(user);
        return createdUserEntity;
    }

    public Authentication getAuthentication(UserRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()));
        return authentication;
    }

    public String getSecurityContextAndJwt(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    public User getUserDetails(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    public List<String> getRoles(User userDetails) {
        return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
    }

    public JwtResponse createJwtResponse(String jwt, String username, List<String> roles) {
        return new JwtResponse(jwt, username, roles);
    }
}
