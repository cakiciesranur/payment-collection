package com.eny.paymentcollection.service.impl;

import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;
import com.eny.paymentcollection.enums.RoleName;
import com.eny.paymentcollection.exception.EmailAlreadyExistsException;
import com.eny.paymentcollection.exception.ResourceNotFoundException;
import com.eny.paymentcollection.exception.UsernameAlreadyExistsException;
import com.eny.paymentcollection.mapper.UserMapper;
import com.eny.paymentcollection.model.RoleEntity;
import com.eny.paymentcollection.model.UserEntity;
import com.eny.paymentcollection.repository.RoleRepository;
import com.eny.paymentcollection.repository.UserRepository;
import com.eny.paymentcollection.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(SignUpDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRoles(resolveRoles(request.getRoles()));

        UserEntity saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<UserEntity> userList = userRepository.findAll();

        if (!userList.isEmpty()) {
            return userMapper.toResponseList(userList);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UpdateUserDto dto) {
        UserEntity user = userRepository.findByUsernameOrEmail(dto.getUsername(), dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userMapper.updateEntityFromDto(dto, user);
        UserEntity updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponseDto getByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        return userMapper.toResponse(user);
    }

    private Set<RoleEntity> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            RoleEntity defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found"));
            return Set.of(defaultRole);
        }

        return roleNames.stream()
                .map(name -> {
                    RoleName roleEnum;
                    try {
                        roleEnum = RoleName.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid role name: " + name);
                    }
                    return roleRepository.findByName(roleEnum)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + name));
                })
                .collect(Collectors.toSet());
    }

}