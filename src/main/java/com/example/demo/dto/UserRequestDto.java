package com.example.demo.dto;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * User Request DTO
 * Contains User information along with nested Address details
 * Supports creating or updating User with their associated Address
 */
@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Invalid mobile number - must be 10 digits"
    )
    private String mobileNumber;

    private Long departmentId;

    private Set<Long> roleIds;

    /**
     * Nested Address object
     * @Valid annotation triggers cascading validation
     * Address details are included in user creation/update request
     */
    @Valid
    private AddressRequestDto address;
}
