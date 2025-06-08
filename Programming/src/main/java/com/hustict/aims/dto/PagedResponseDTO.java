package com.hustict.aims.dto;

import java.util.List;

/**
 * Generic DTO for paginated responses
 * Provides pagination metadata along with the data
 * Follows Generic programming and reduces code duplication
 */
public class PagedResponseDTO<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean first;
    private boolean last;

    public PagedResponseDTO() {}

    public PagedResponseDTO(List<T> content, long totalElements, int totalPages, 
                            int currentPage, int pageSize, boolean first, boolean last) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.first = first;
        this.last = last;
    }

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }

    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
} 