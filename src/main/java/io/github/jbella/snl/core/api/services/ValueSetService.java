package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.ValueSet;

import java.util.List;

import static io.github.jbella.snl.core.api.domain.ValueSet.BaseView;

public interface ValueSetService {
    List<BaseView> getValuesForSystem(String system, String lang, Boolean active);

    String getDisplay(String system, String code, String lang);

    List<ValueSet.BaseView> getValuesByCategoryKey(String category, String key, String lang, Boolean active);

    String getDisplayByCategoryKey(String category, String key, String code, String lang);
}
