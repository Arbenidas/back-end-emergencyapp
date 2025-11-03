package com.delivey.apirest.controller;

import com.delivey.apirest.dto.user.*;
import com.delivey.apirest.models.User;
import com.delivey.apirest.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping()
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request){
        CreateUserResponse user = userServices.create(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            CreateUserResponse response = userServices.findById(id);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",HttpStatus.NOT_FOUND.value()
            ));
        }

    }
    @PutMapping(value = "/upload/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, @ModelAttribute UpdateUserRequest request){
        try {
            CreateUserResponse response = userServices.updateUserWithImage(id,request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException | IOException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message",e.getMessage(),
                    "statusCode",HttpStatus.NOT_FOUND.value()
            ));
        }

    }

}
