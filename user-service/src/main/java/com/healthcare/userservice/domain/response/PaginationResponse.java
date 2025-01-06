package com.healthcare.userservice.domain.response;

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

    public PaginationResponse(List<T> data, Long totalItems) {
        this.currentPage = 0; // Non-paginated response treated as one "page."
        this.pageSize = data != null ? data.size() : 0;
        this.totalItems = totalItems;
        this.totalPages = 1; // Non-paginated response has one "page."
        this.data = data;
    }
}
