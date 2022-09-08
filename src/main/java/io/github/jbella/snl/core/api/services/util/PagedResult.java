package io.github.jbella.snl.core.api.services.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PagedResult<T> {
    private List<T> data;
    private long totalSize;
    private long totalPages;
}