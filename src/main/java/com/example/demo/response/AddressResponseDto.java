package com.example.demo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Address Response DTO
 * Returned in API responses when User information includes Address
 * Includes complete address details without exposing database internals
 */
@Getter
@Setter
@Builder
public class AddressResponseDto {

    private Long id;

    private String city;

    private String state;

    private String country;

    private String postalCode;
}
