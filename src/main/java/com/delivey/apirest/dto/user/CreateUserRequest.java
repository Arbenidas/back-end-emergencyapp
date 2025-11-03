package com.delivey.apirest.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Generated
public class CreateUserRequest {

    public String name;
    public String lastname;
    public String email;
    public String phone;
    public String password;
    public String dui;
}
