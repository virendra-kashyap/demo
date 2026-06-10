package com.example.demo.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class UserSpecification {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isBlank()) {
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastName")),
                    "%" + lastName.toLowerCase(Locale.ROOT) + "%");
        };
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("email")),
                    email.toLowerCase(Locale.ROOT));
        };
    }

    public static Specification<User> hasMobileNumber(String mobileNumber) {
        return (root, query, criteriaBuilder) -> {
            if (mobileNumber == null || mobileNumber.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("mobileNumber"), mobileNumber);
        };
    }

    public static Specification<User> hasDepartment(String departmentName) {
        return (root, query, criteriaBuilder) -> {
            if (departmentName == null || departmentName.isBlank()) {
                return null;
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("department").get("name")),
                    departmentName.toLowerCase(Locale.ROOT));
        };
    }

    public static Specification<User> hasRole(String roleName) {
        return (root, query, criteriaBuilder) -> {
            if (roleName == null || roleName.isBlank()) {
                return null;
            }
            query.distinct(true);
            Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT);
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(roleJoin.get("name")),
                    roleName.toLowerCase(Locale.ROOT));
        };
    }

    public static Specification<User> hasCreatedDate(String createdDate) {
        return (root, query, criteriaBuilder) -> {
            if (createdDate == null || createdDate.isBlank()) {
                return null;
            }
            LocalDate date = LocalDate.parse(createdDate, DATE_FORMATTER);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59, 999_999_999);
            return criteriaBuilder.between(root.get("createdDate"), start, end);
        };
    }
}
