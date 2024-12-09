package com.health.care.appointment.service.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> implements Serializable {

    private Integer currentPage;
    private Integer pageSize;
    private Long totalItems;
    private Integer totalPages;
    private List<T> data;
}

