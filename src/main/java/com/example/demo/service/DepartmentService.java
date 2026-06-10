package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.DepartmentRequestDto;
import com.example.demo.response.DepartmentResponseDto;

public interface DepartmentService {

    DepartmentResponseDto createDepartment(DepartmentRequestDto request);

    DepartmentResponseDto getDepartment(Long id);

    DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto request);

    void deleteDepartment(Long id);

    List<DepartmentResponseDto> getAllDepartments();

}
