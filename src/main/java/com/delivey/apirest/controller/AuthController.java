package com.delivey.apirest.controller;

import com.delivey.apirest.dto.user.CreateUserRequest;
import com.delivey.apirest.dto.user.CreateUserResponse;
import com.delivey.apirest.dto.user.LoginRequest;
import com.delivey.apirest.dto.user.LoginResponse;
import com.delivey.apirest.models.User;
import com.delivey.apirest.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserServices userServices;

    @PostMapping(value = "/register")
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request){
        try {
            CreateUserResponse user = userServices.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",HttpStatus.BAD_REQUEST.value()
            ));
        }

    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            LoginResponse response = userServices.login(request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",HttpStatus.UNAUTHORIZED.value()
            ));
        }

    }

}
