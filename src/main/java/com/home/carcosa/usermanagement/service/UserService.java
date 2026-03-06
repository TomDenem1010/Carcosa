package com.home.carcosa.usermanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.home.carcosa.usermanagement.Role;
import com.home.carcosa.usermanagement.database.repository.UserRepository;
import com.home.carcosa.usermanagement.dto.UserCreateRequest;
import com.home.carcosa.usermanagement.dto.UserCreateResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "USER")
@Service
@AllArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCreateResponse createUser(UserCreateRequest request){
        try{
            userRepository.insert(new com.home.carcosa.usermanagement.database.dao.User(null, request.name(),
                    passwordEncoder.encode(request.password()), request.role()));
        } catch (Exception e){
            log.error("Failed to create user: {}",request,e);
            return new UserCreateResponse("Failed to create user!");
        }
        return new UserCreateResponse("User created successfully");
    }

    public boolean checkUserHasRole(String name,String password,Role role){
        var user = userRepository.findByName(name).stream().findFirst();
        if (user.isEmpty()){
            return false;
        }
        var userData = user.get();
        if (!passwordEncoder.matches(password,userData.getPassword())){
            return false;
        }
        return role.equals(Role.fromString(userData.getRole()));
    }
}
