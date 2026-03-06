package com.home.carcosa.usermanagement.dto;

import com.home.carcosa.usermanagement.Role;

public record User(String id, String name, String password, Role role) {

}
