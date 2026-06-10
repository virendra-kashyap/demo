package com.example.demo.mapper;

import org.mapstruct.Mapper;
import com.example.demo.dto.AddressRequestDto;
import com.example.demo.entity.Address;
import com.example.demo.response.AddressResponseDto;

/**
 * MapStruct Mapper for Address entity
 * 
 * Handles bidirectional conversion:
 * - AddressRequestDto → Address entity
 * - Address entity → AddressResponseDto
 * 
 * Key benefits:
 * - Type-safe mapping
 * - Compile-time generation
 * - Zero runtime reflection overhead
 * - Null safety
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    /**
     * Convert AddressRequestDto to Address entity
     * Used when creating/updating Address from API request
     */
    Address toEntity(AddressRequestDto request);

    /**
     * Convert Address entity to AddressResponseDto
     * Used when returning Address in API response
     * Does not include bidirectional User reference to avoid recursion
     */
    AddressResponseDto toResponse(Address address);
}
