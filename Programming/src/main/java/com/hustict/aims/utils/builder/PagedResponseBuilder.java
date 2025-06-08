package com.hustict.aims.utils.builder;

import com.hustict.aims.dto.PagedResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class PagedResponseBuilder {
    public static <T, U> PagedResponseDTO<U> fromPage(Page<T> page, Function<T, U> mapper) {
        List<U> content = page.getContent().stream().map(mapper).toList();

        return new PagedResponseDTO<>(
            content,
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize(),
            page.isFirst(),
            page.isLast()
        );
    }
}

