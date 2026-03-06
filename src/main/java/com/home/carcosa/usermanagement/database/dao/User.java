package com.home.carcosa.usermanagement.database.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("user")
@Data
public class User{

    @Id
    private final String id;
    private final String name;
    private final String password;
    private final String role;
}
