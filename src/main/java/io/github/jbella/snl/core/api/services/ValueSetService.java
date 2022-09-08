package io.github.jbella.snl.core.api.services;

import java.util.List;

import static io.github.jbella.snl.core.api.domain.ValueSet.BaseView;

public interface ValueSetService  {
    List<BaseView> getValuesForSystem(String system, String lang, Boolean active);

    String getDisplay(String system, String code, String lang);
}
