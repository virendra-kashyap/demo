package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.UserResponseDto;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User APIs", description = "User Management APIs")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @Operation(summary = "Create User", description = "Creates a new user")
        @PostMapping
        public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
                        @Valid @RequestBody UserRequestDto request) {

                log.info("Create user request received: {}", request);
                UserResponseDto response = userService.createUser(request);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.<UserResponseDto>builder()
                                                .success(true)
                                                .message("User created successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Get User By Id", description = "Fetch user details using ID")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponseDto>> getUser(
                        @PathVariable Long id) {

                log.info("Fetch user request received for id={}", id);
                UserResponseDto response = userService.getUser(id);

                return ResponseEntity.ok(
                                ApiResponse.<UserResponseDto>builder()
                                                .success(true)
                                                .message("User fetched successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Update User", description = "Update user details using ID")
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
                        @PathVariable Long id,
                        @Valid @RequestBody UserRequestDto request) {

                log.info("Update user request received for id={} payload={}", id, request);
                UserResponseDto response = userService.updateUser(id, request);

                return ResponseEntity.ok(
                                ApiResponse.<UserResponseDto>builder()
                                                .success(true)
                                                .message("User updated successfully")
                                                .data(response)
                                                .build());
        }

        @Operation(summary = "Delete User", description = "Delete user using ID")
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<String>> deleteUser(
                        @PathVariable Long id) {

                log.info("Delete user request received for id={}", id);
                userService.deleteUser(id);

                return ResponseEntity.ok(
                                ApiResponse.<String>builder()
                                                .success(true)
                                                .message("User deleted successfully")
                                                .data("Deleted")
                                                .build());
        }

        @Operation(summary = "Get Users", description = "Fetch list of users")
        @GetMapping
        public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction) {

                log.info("Get users request received page={} size={} sortBy={} direction={}",
                                page,
                                size,
                                sortBy,
                                direction);
                return ResponseEntity.ok(
                                ApiResponse.<Page<UserResponseDto>>builder()
                                                .success(true)
                                                .message("Users fetched successfully")
                                                .data(userService.getUsers(page, size, sortBy, direction))
                                                .build());
        }

        @Operation(summary = "Search Users", description = "Search users by name")
        @GetMapping("/search")
        public ResponseEntity<ApiResponse<Page<UserResponseDto>>> searchUsers(
                        @RequestParam String name,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction) {

                log.info("Search users request received name={} page={} size={} sortBy={} direction={}",
                                name,
                                page,
                                size,
                                sortBy,
                                direction);
                return ResponseEntity.ok(
                                ApiResponse.<Page<UserResponseDto>>builder()
                                                .success(true)
                                                .message("Users fetched successfully")
                                                .data(userService.searchUsers(
                                                                name,
                                                                page,
                                                                size,
                                                                sortBy,
                                                                direction))
                                                .build());
        }

}
