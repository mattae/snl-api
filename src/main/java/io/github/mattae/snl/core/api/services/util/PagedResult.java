package io.github.mattae.snl.core.api.services.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResult<T> {
    private List<T> data;
    private long totalSize;
    private long totalPages;
}
