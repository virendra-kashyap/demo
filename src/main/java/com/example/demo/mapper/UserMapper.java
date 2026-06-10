package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.response.UserResponseDto;

/**
 * MapStruct Mapper for User entity
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequestDto request);

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "address", source = "address")
    UserResponseDto toResponse(User user);
}
