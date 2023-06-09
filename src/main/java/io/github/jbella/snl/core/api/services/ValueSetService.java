package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.ValueSet;
import io.github.jbella.snl.core.api.services.util.PagedResult;

import java.util.Set;

public interface ValueSetService {
    Set<ValueSet.Value> getValuesForSystem(String system, String lang, Boolean active);

    String getDisplay(String system, String code, String lang);

    Set<ValueSet.Value> getValuesByCategoryKey(String category, String key, String lang, Boolean active);

    PagedResult<ValueSet.SystemView> getValueSets(String keyword, int start, int pageSize);

    String getDisplayByCategoryKey(String category, String key, String code, String lang);

    void deleteById(Long id);
}
