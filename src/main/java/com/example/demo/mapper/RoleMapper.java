package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.RoleRequestDto;
import com.example.demo.entity.Role;
import com.example.demo.response.RoleResponseDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleRequestDto request);

    RoleResponseDto toResponse(Role role);
}
