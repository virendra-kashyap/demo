package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.DepartmentRequestDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.response.DepartmentResponseDto;
import com.example.demo.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Department APIs", description = "Department Management APIs")
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Create Department")
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> create(
            @Valid @RequestBody DepartmentRequestDto request) {
        log.info("Create department request: {}", request.getName());
        DepartmentResponseDto resp = departmentService.createDepartment(request);
        return ResponseEntity.ok(ApiResponse.<DepartmentResponseDto>builder().success(true).data(resp)
                .message("Department created").build());
    }

    @Operation(summary = "Get Department by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> get(@PathVariable Long id) {
        DepartmentResponseDto resp = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.<DepartmentResponseDto>builder().success(true).data(resp)
                .message("Department fetched").build());
    }

    @Operation(summary = "Update Department")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> update(@PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto request) {
        DepartmentResponseDto resp = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.<DepartmentResponseDto>builder().success(true).data(resp)
                .message("Department updated").build());
    }

    @Operation(summary = "Delete Department")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.<String>builder().success(true).data("Deleted")
                .message("Department deleted").build());
    }

    @Operation(summary = "List Departments")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> list() {
        return ResponseEntity.ok(ApiResponse.<List<DepartmentResponseDto>>builder().success(true)
                .data(departmentService.getAllDepartments()).message("Departments fetched").build());
    }

}
