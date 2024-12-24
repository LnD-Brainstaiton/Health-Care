package com.health_care.user_service.repository.specification;

import com.health_care.user_service.domain.entity.TempData;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TempDataSpecifications {
    public static Specification<TempData> buildSpecification(
            String featureCode,
            String requestId,
            String startDate,
            String endDate,
            Boolean operationType,
            String userId, Boolean aTrue) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (featureCode != null && !featureCode.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("featureCode"), featureCode));
            }

            if (requestId != null && !requestId.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("requestId"), requestId));
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get("createdDate"),
                        LocalDateTime.parse(startDate),
                        LocalDateTime.parse(endDate)
                ));
            }

            if (operationType != null) {
                predicates.add(criteriaBuilder.equal(root.get("operationType"), operationType));
            }

            predicates.add(criteriaBuilder.notEqual(root.get("makerId"), userId));

            predicates.add(criteriaBuilder.equal(root.get("isActive"), aTrue));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}