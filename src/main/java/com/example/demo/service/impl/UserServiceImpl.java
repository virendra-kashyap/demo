package com.example.demo.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.Address;
import com.example.demo.entity.Department;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.DepartmentNotFoundException;
import com.example.demo.exception.RoleNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.AddressMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserResponseDto;
import com.example.demo.service.UserService;
import com.example.demo.specification.UserSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * User Service Implementation
 *
 * Handles User CRUD operations including:
 * - Creating users with department, address, and roles
 * - Updating user information and associations
 * - Soft delete and restore operations
 * - Dynamic filtering using JPA Specifications
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "firstName", "lastName", "email", "createdDate", "modifiedDate");

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        log.info("Creating user with firstName={} lastName={} email={} hasDepartment={} hasAddress={} hasRoles={}",
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getDepartmentId() != null,
                request.getAddress() != null,
                request.getRoleIds() != null && !request.getRoleIds().isEmpty());

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            "Department not found with id " + request.getDepartmentId()));
        }

        Address address = null;
        if (request.getAddress() != null) {
            address = addressMapper.toEntity(request.getAddress());
            log.debug("Address created from request: city={} state={} country={}",
                    address.getCity(),
                    address.getState(),
                    address.getCountry());
        }

        Set<Role> roles = resolveRoles(request.getRoleIds());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .department(department)
                .address(address)
                .roles(roles)
                .build();

        user = userRepository.save(user);
        log.info("User created with id={} roles={}", user.getId(), roles.stream().map(Role::getName).toList());
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        log.info("Retrieving user by id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        log.info("Updating user id={} with request={}", id, request);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(
                            "Department not found with id " + request.getDepartmentId()));
            user.setDepartment(department);
        }

        if (request.getAddress() != null) {
            if (user.getAddress() == null) {
                Address newAddress = addressMapper.toEntity(request.getAddress());
                user.setAddress(newAddress);
            } else {
                Address existingAddress = user.getAddress();
                existingAddress.setCity(request.getAddress().getCity());
                existingAddress.setState(request.getAddress().getState());
                existingAddress.setCountry(request.getAddress().getCountry());
                existingAddress.setPostalCode(request.getAddress().getPostalCode());
            }
        }

        if (request.getRoleIds() != null) {
            Set<Role> roles = resolveRoles(request.getRoleIds());
            user.setRoles(roles);
        }

        userRepository.save(user);
        log.info("User updated successfully id={}", id);
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Soft deleting user id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.delete(user);
        log.info("User marked as deleted id={}", id);
    }

    @Override
    public UserResponseDto restoreUser(Long id) {
        log.info("Restoring user id={}", id);
        userRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        userRepository.restoreById(id);
        return getUser(id);
    }

    @Override
    public Page<UserResponseDto> getUsers(int page, int size, String sortBy, String direction) {
        log.info("Listing users page={} size={} sortBy={} direction={}", page, size, sortBy, direction);
        validateSort(sortBy, direction);
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    public Page<UserResponseDto> searchUsers(String firstName, String lastName, String email, String mobileNumber,
            String department, String role, String createdDate, int page, int size, String sortBy, String direction) {
        log.info("Searching users firstName={} lastName={} email={} mobileNumber={} department={} role={} createdDate={} page={} size={} sortBy={} direction={}",
                firstName, lastName, email, mobileNumber, department, role, createdDate, page, size, sortBy, direction);

        validateSort(sortBy, direction);
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> specification = Specification.where(UserSpecification.hasFirstName(firstName))
                .and(UserSpecification.hasLastName(lastName))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasMobileNumber(mobileNumber))
                .and(UserSpecification.hasDepartment(department))
                .and(UserSpecification.hasRole(role))
                .and(UserSpecification.hasCreatedDate(createdDate));

        return userRepository.findAll(specification, pageable).map(userMapper::toResponse);
    }

    private void validateSort(String sortBy, String direction) {
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            log.warn("Invalid sort field requested: {}", sortBy);
            throw new IllegalArgumentException("Invalid sort field");
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            log.warn("Invalid sort direction requested: {}", direction);
            throw new IllegalArgumentException("Direction must be asc or desc");
        }
    }

    private Sort getSort(String sortBy, String direction) {
        return direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    }

    private Set<Role> resolveRoles(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            Set<Long> foundIds = roles.stream()
                    .map(Role::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Set<Long> missing = roleIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());
            throw new RoleNotFoundException("Roles not found for ids: " + missing);
        }
        return new HashSet<>(roles);
    }
}