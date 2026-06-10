package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RoleRequestDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.RoleResponseDto;
import com.example.demo.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Role APIs", description = "Role Management APIs")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create Role", description = "Create a new role")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(
            @Valid @RequestBody RoleRequestDto request) {
        log.info("Create role request received: {}", request);
        RoleResponseDto response = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponseDto>builder()
                        .success(true)
                        .message("Role created successfully")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Get Role By Id", description = "Fetch a role by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponseDto>> getRole(@PathVariable Long id) {
        log.info("Fetch role request received for id={}", id);
        RoleResponseDto response = roleService.getRole(id);
        return ResponseEntity.ok(ApiResponse.<RoleResponseDto>builder()
                .success(true)
                .message("Role fetched successfully")
                .data(response)
                .build());
    }

    @Operation(summary = "Get All Roles", description = "Fetch all available roles")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> getRoles() {
        log.info("Fetch all roles request received");
        return ResponseEntity.ok(ApiResponse.<List<RoleResponseDto>>builder()
                .success(true)
                .message("Roles fetched successfully")
                .data(roleService.getRoles())
                .build());
    }
}
