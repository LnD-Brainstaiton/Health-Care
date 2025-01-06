package com.healthcare.userservice.repository.specification;

import com.healthcare.userservice.domain.entity.Doctor;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    public static Specification<Doctor> hasDesignation(String designation) {
        return (root, query, criteriaBuilder) -> {
            if (designation == null || designation.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("designation"), designation);
        };
    }

    public static Specification<Doctor> hasDepartment(String department) {
        return (root, query, criteriaBuilder) -> {
            if (department == null || department.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("department"), department);
        };
    }

    public static Specification<Doctor> hasSpecialities(String gender) {
        return (root, query, criteriaBuilder) -> {
            if (gender == null || gender.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("gender"), gender);
        };
    }

    public static Specification<Doctor> hasFirstnameLastname(String firstnameLastname) {
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

    public static Specification<Doctor> hasId(String id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchTerm = "%" + id.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("doctorId")), searchTerm)
            );
        };
    }

    public static Specification<Doctor> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));
    }
}
