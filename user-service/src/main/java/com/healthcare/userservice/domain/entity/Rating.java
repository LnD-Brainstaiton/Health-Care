package com.healthcare.userservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( name = "rating")
public class Rating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String ratingId;

    private BigDecimal rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column(nullable = false, length = 50)
    private String doctorId;

    @Column(nullable = false, length = 50)
    private String commentId;

    @Column(nullable = false, length = 50)
    private String commentParentId;

    private LocalDateTime commentTime;

    @Override
    public String toString() {
        return "id: " + id + ", ratingId: " + ratingId + ", commentId: " + commentId + ", commentParentId: " + commentParentId;
    }

}
