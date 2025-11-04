package com.delivey.apirest.dto.user.mapper;

import com.delivey.apirest.config.APIConfig;
import com.delivey.apirest.dto.role.RoleDTO;
import com.delivey.apirest.dto.user.UserResponse;
import com.delivey.apirest.models.Role;
import com.delivey.apirest.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, List<Role> roles){

        List<RoleDTO> roleDTOS = roles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getImage(),role.getImage(),role.getRoute()))
                .toList();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setLastname(user.getLastname());
        response.setRoles(roleDTOS);

        if (user.getImage() !=null){
            String imgUrl = APIConfig.BASE_URL + user.getImage();
            response.setImage(imgUrl);
        }

        return response;
    }
}
