package com.example.demo.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserResponseDto;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "firstName", "lastName", "email");

    @Override
    public UserResponseDto createUser(UserRequestDto request) {

        log.info("Creating user with firstName={} lastName={} email={}",
                request.getFirstName(),
                request.getLastName(),
                request.getEmail());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .build();

        user = userRepository.save(user);

        log.info("User created with id={}", user.getId());
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto getUser(Long id) {
        log.info("Retrieving user by id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        log.info("Updating user id={} with request={}", id, request);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        userRepository.save(user);

        log.info("User updated successfully id={}", id);
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id " + id));

        userRepository.delete(user);
        log.info("User deleted successfully id={}", id);
    }

    @Override
    public Page<UserResponseDto> getUsers(int page, int size, String sortBy, String direction) {

        log.info("Listing users page={} size={} sortBy={} direction={}",
                page,
                size,
                sortBy,
                direction);

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            log.warn("Invalid sort field requested: {}", sortBy);
            throw new IllegalArgumentException("Invalid sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            log.warn("Invalid sort direction requested: {}", direction);
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public Page<UserResponseDto> searchUsers(
            String name,
            int page,
            int size,
            String sortBy,
            String direction) {

        log.info("Searching users name={} page={} size={} sortBy={} direction={}",
                name,
                page,
                size,
                sortBy,
                direction);

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            log.warn("Invalid sort field requested: {}", sortBy);
            throw new IllegalArgumentException(
                    "Invalid sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            log.warn("Invalid sort direction requested: {}", direction);
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository
                .findByFirstNameContainingIgnoreCase(
                        name,
                        pageable)
                .map(userMapper::toResponse);
    }

}
