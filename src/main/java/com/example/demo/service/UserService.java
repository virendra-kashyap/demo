package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.response.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserRequestDto request);

    UserResponseDto getUser(Long id);

    UserResponseDto updateUser(
            Long id,
            UserRequestDto request);

    void deleteUser(Long id);

    Page<UserResponseDto> getUsers(int page, int size, String sortBy, String direction);

    Page<UserResponseDto> searchUsers(
            String name,
            int page,
            int size,
            String sortBy,
            String direction);

}
