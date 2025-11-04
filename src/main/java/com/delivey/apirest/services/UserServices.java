package com.delivey.apirest.services;

import com.delivey.apirest.config.PasswordEncoderConfig;
import com.delivey.apirest.dto.role.RoleDTO;
import com.delivey.apirest.dto.user.*;
import com.delivey.apirest.dto.user.mapper.UserMapper;
import com.delivey.apirest.models.Role;
import com.delivey.apirest.models.User;
import com.delivey.apirest.models.UserHasRoles;
import com.delivey.apirest.repository.RoleRepository;
import com.delivey.apirest.repository.UserHasRolesRepository;
import com.delivey.apirest.repository.UserRepository;
import com.delivey.apirest.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoderConfig passwordEncoderConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository repository;
    @Autowired
    private UserHasRolesRepository userHasRolesRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public LoginResponse create(CreateUserRequest request){
        if (userRepository.existsByEmail(request.email)){
            throw new RuntimeException("Correo ya registrado");
        };
        User user = new User();
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());


        //Encrypta la contraseña para mandarla al dto
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);

        user.setDui(request.getDui());
        User savedUser = userRepository.save(user);
        Role clientRole = roleRepository.findById("CLIENT").orElseThrow(
                ()-> new RuntimeException("El role de cliente no existe")
        );

        UserHasRoles userHasRoles = new UserHasRoles(savedUser,clientRole);
        userHasRolesRepository.save(userHasRoles);


        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(savedUser.getId());

        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(userMapper.toUserResponse(user,roles));

        return response;
    }

    @Transactional
    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException(("El email o password no son validos")));
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("El email o password no son validos");
        }

        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());




        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(userMapper.toUserResponse(user,roles));

        return  response;

    }

    @Transactional
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("El email o password no son validos")));



        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());


        return userMapper.toUserResponse(user,roles);

}
    @Transactional
    public UserResponse updateUserWithImage(Long id, UpdateUserRequest request) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("El email o password no son validos")));

        //condiciones para cambiar los datos de un usuario
        if (request.getName() !=null){
            user.setName(request.getName());

        }
        if (request.getLastname() !=null){
            user.setLastname(request.getLastname());

        }
        if (request.getPhone() !=null){
            user.setPhone(request.getPhone());

        }
        if (request.getFile() != null && !request.getFile().isEmpty()){
            String uploadDir = "uploads/users/" + user.getId();
            String filename = request.getFile().getOriginalFilename();
            String filePath = Paths.get(uploadDir,filename).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(),Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/"+ filePath.replace("\\","/"));
        }
        userRepository.save(user);

        List<Role> roles = roleRepository.findAllByUserHasRoles_User_Id(user.getId());

       return userMapper.toUserResponse(user,roles);

    }
}
