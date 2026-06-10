package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.DepartmentRequestDto;
import com.example.demo.entity.Department;
import com.example.demo.exception.DepartmentNotFoundException;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.response.DepartmentResponseDto;
import com.example.demo.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto request) {
        log.info("Creating department name={}", request.getName());
        Department dept = Department.builder().name(request.getName()).build();
        dept = departmentRepository.save(dept);
        return toDto(dept);
    }

    @Override
    public DepartmentResponseDto getDepartment(Long id) {
        log.info("Retrieving department id={}", id);
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + id));
        return toDto(dept);
    }

    @Override
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto request) {
        log.info("Updating department id={} name={}", id, request.getName());
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + id));
        dept.setName(request.getName());
        dept = departmentRepository.save(dept);
        return toDto(dept);
    }

    @Override
    public void deleteDepartment(Long id) {
        log.info("Deleting department id={}", id);
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + id));
        departmentRepository.delete(dept);
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private DepartmentResponseDto toDto(Department d) {
        return DepartmentResponseDto.builder().id(d.getId()).name(d.getName()).build();
    }

}
