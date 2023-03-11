package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.ValueSet;
import io.github.jbella.snl.core.api.services.util.PagedResult;

import java.util.List;

import static io.github.jbella.snl.core.api.domain.ValueSet.BaseView;

public interface ValueSetService {
    List<ValueSet.Value> getValuesForSystem(String system, String lang, Boolean active);

    String getDisplay(String system, String code, String lang);

    List<ValueSet.Value> getValuesByCategoryKey(String category, String key, String lang, Boolean active);

    PagedResult<ValueSet.SystemView> getValueSets(String keyword, int start, int pageSize);

    String getDisplayByCategoryKey(String category, String key, String code, String lang);
}
