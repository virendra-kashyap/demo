package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.response.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto request);
    
    UserResponseDto toResponse(User user);
}
