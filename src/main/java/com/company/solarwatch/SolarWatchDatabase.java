package com.company.solarwatch;

import com.company.solarwatch.model.RoleType;
import com.company.solarwatch.model.UserEntity;
import com.company.solarwatch.model.solarWatchData.Role;
import com.company.solarwatch.repository.UserEntityRepository;
import com.company.solarwatch.service.RoleService;
import com.company.solarwatch.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@SpringBootApplication
public class SolarWatchDatabase {

    public static void main(String[] args) {
        SpringApplication.run(SolarWatchDatabase.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            RoleService roleService,
            UserEntityRepository userEntityRepository,
            PasswordEncoder passwordEncoder) {
        return runner -> {
            Role userrole = new Role(RoleType.ROLE_USER);
            Role adminrole = new Role(RoleType.ROLE_ADMIN);
            roleService.createRole(userrole);
            roleService.createRole(adminrole);
            Role role = roleService.findByName(RoleType.ROLE_USER);
            System.out.println(role);
            UserEntity ela = new UserEntity("ela",
                    passwordEncoder.encode("ela"), Set.of(role));
            System.out.println(ela);
            userEntityRepository.save(ela);
        };
    }
}
