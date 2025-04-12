package com.doomdev.admin_blog.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageResponse<T> extends DefaultListResponse<T> {

    private Pagination pagination;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Pagination {
        private Integer page;

        private Integer pageSize;

        private Integer totalPages;

        private Long totalElements;
    }

    public static <E> PageResponse<E> success(Page<?> page, List<E> data) {
        var resp = new PageResponse<E>();
        resp.success();
        resp.pagination = new Pagination();
        resp.pagination.setPage(page.getNumber());
        resp.pagination.setPageSize(page.getSize());
        resp.pagination.setTotalPages(page.getTotalPages());
        resp.pagination.setTotalElements(page.getTotalElements());
        resp.setData(data);
        return resp;
    }

    public static <E> PageResponse<E> empty(Integer page, Integer pageSize) {
        var resp = new PageResponse<E>();
        resp.success();
        resp.pagination = new Pagination();
        resp.pagination.setPage(page);
        resp.pagination.setPageSize(pageSize);
        resp.pagination.setTotalPages(0);
        resp.pagination.setTotalElements(0L);
        resp.setData(Collections.emptyList());
        return resp;
    }

}
