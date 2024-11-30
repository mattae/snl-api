package io.github.mattae.snl.core.api.services;

import io.github.mattae.snl.core.api.domain.Preference;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.Optional;

public interface PreferenceService {

    Preference save(Preference preference);

    void delete(Preference preference);

    Optional<Preference> getPreference(@Nonnull String category);

    Optional<Double> getNumericValueForKey(@Nonnull String category, @Nonnull String key);

    Optional<String> getStringValueForKey(@Nonnull String category, @Nonnull String key);

    Optional<Boolean> getBoolValueForKey(@Nonnull String category, @Nonnull String key);

    Optional<LocalDate> getDateValueForKey(@Nonnull String category, @Nonnull String key);
}
