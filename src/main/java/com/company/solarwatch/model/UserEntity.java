package com.company.solarwatch.model;

import java.util.Set;

public record UserEntity(String username, String password, Set<Role> roles) {
}
