package com.company.solarwatch.controller;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.payload.JwtResponse;
import com.company.solarwatch.model.payload.UserRequest;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.UserEntityRepository;
import com.company.solarwatch.security.jwt.JwtUtils;
import com.company.solarwatch.service.RoleService;
import com.company.solarwatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserEntityController {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public UserEntityController(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RoleService roleService, UserService userService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.userService = userService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<Void> createUser(@RequestBody UserRequest signUpRequest) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(signUpRequest.getUsername());
        if (userEntity != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        userService.createUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register_admin")
    public ResponseEntity<Void> createAdmin(@RequestBody UserRequest signUpRequest) {
        UserEntity userEntity = userEntityRepository.findUserEntityByUsername(signUpRequest.getUsername());
        if (userEntity != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        userService.createAdmin(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody UserRequest loginRequest) {

        System.out.println(loginRequest.toString());
        Authentication authentication = userService.getAuthentication(loginRequest);
        String jwt = userService.getSecurityContextAndJwt(authentication);

        User userDetails = userService.getUserDetails(authentication);
        List<String> roles = userService.getRoles(userDetails);
        JwtResponse jwtResponse = userService.createJwtResponse(jwt, userDetails.getUsername(), roles);

        return ResponseEntity.ok(jwtResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public String me() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return "Hello " + user.getUsername();
    }

    @GetMapping("/authorize")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> authorize() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public\n";
    }
}
