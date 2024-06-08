package com.hart.overwatch.pagination.dto;


import java.util.List;

public class PaginationDto<T> {

    private List<T> items;

    private int page;

    private int pageSize;

    private int totalPages;

    private String direction;

    private long totalElements;

    public PaginationDto() {

    }

    public PaginationDto(List<T> items, int page, int pageSize, int totalPages, String direction,
            long totalElements) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.direction = direction;
        this.totalElements = totalElements;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getDirection() {
        return direction;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
