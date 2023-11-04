package com.company.solarwatch.model.solarWatchData;

import com.company.solarwatch.model.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Table(name="roles")
public class Role {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role() {

    }

    public Role(RoleType name) {
        this.name = name;
    }
}
