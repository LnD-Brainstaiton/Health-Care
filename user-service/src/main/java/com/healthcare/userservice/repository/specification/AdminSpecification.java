package com.healthcare.userservice.repository.specification;

import com.healthcare.userservice.domain.entity.Admin;
import org.springframework.data.jpa.domain.Specification;

public class AdminSpecification {

    public static Specification<Admin> hasFirstnameLastname(String firstnameLastname) {
        return (root, query, criteriaBuilder) -> {
            if (firstnameLastname == null || firstnameLastname.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + firstnameLastname.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), searchTerm),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), searchTerm)
            );
        };
    }

    public static Specification<Admin> hasId(String id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + id.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("adminId")), searchTerm)
            );
        };
    }

    public static Specification<Admin> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));
    }
}
