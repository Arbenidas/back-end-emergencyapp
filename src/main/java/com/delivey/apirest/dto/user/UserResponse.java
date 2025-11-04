package com.delivey.apirest.dto.user;

import com.delivey.apirest.dto.role.RoleDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse {
    public Long id;
    public String name;
    public String lastname;
    public String email;
    public String phone;
    public String image;

    @JsonProperty("notification_token")
    public String notificationToken;

    List<RoleDTO> roles;
}
