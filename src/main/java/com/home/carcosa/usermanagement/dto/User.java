package com.home.carcosa.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.home.carcosa.usermanagement.Role;

public record User(String id, String name, @JsonIgnore String password, Role role) {

}
