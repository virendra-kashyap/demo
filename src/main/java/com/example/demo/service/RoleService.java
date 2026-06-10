package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.RoleRequestDto;
import com.example.demo.response.RoleResponseDto;

public interface RoleService {

    RoleResponseDto createRole(RoleRequestDto request);

    RoleResponseDto getRole(Long id);

    List<RoleResponseDto> getRoles();
}
