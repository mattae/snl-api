package io.github.mattae.snl.core.api.services;

import io.github.mattae.snl.core.api.domain.Configuration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConfigurationService {
    List<Configuration.CreateView> list(String category, String key);

    Optional<String> getValueAsStringForKey(String category, String key);

    Optional<Boolean> getValueAsBooleanForKey(String category, String key);

    Optional<Double> getValueAsNumericForKey(String category, String key);

    Optional<LocalDate> getValueAsDateForKey(String category, String key);

    void deleteById(Long id);
}
