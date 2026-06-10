package com.example.demo.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User Response DTO
 * Contains complete User information including Department, Role, and Address details
 * Returned in all user-related API responses
 */
@Getter
@Setter
@Builder
public class UserResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private Long departmentId;

    private String departmentName;

    private Set<RoleResponseDto> roles;

    private AddressResponseDto address;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String createdBy;

    private String modifiedBy;
}
