package com.health_care.gateway.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@Getter
@Setter
@Table("GATEWAY_PUBLIC_API")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicApi {

    @Id
    private Long id;

    @Column("url")
    private String url;

    @Column("is_active")
    private Boolean isActive;
}
