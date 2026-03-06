package com.home.carcosa.usermanagement.database.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.home.carcosa.usermanagement.database.dao.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    List<User> findByName(String name);
}
