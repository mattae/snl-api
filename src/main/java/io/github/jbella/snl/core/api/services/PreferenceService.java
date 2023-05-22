package io.github.jbella.snl.core.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.jbella.snl.core.api.domain.Preference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Optional;

public interface PreferenceService {
    Preference save(Preference preference);

    void delete(Preference preference);

    Optional<Preference> getPreference(@Nonnull String category, @Nullable String key);

    Optional<Integer> getIntValue(@Nonnull String category, @Nullable String key);

    Optional<String> getStringValue(@Nonnull String category, @Nullable String key);

    Optional<Boolean> getBoolValue(@Nonnull String category, @Nullable String key);

    Optional<LocalDate> getDateValue(@Nonnull String category, @Nullable String key);

    Optional<JsonNode> getJsonValue(@Nonnull String category, @Nullable String key);
}
