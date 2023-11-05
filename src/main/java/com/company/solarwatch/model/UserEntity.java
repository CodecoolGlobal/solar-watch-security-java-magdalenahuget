package com.company.solarwatch.model;

import com.company.solarwatch.model.solarWatchData.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@Table(name="user_entities")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    public UserEntity(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserEntity() {
    }
}
