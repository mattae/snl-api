package io.github.mattae.snl.core.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mattae.snl.core.api.domain.Preference;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.Optional;

public interface PreferenceService {
    /**
     * Performs a Preference save operation.
     *
     * @param preference the Preference value to save
     * @return the saved Preference
     * @throws DataIntegrityViolationException if the key in a category is repeated or the category is repeated without keys
     * @throws TransactionSystemException if the category or data is not provided
     */

    Preference save(Preference preference);

    void delete(Preference preference);

    Optional<Preference> getPreference(@Nonnull String category, @Nullable String key);

    Optional<Double> getNumericValue(@Nonnull String category, @Nullable String key);

    Optional<String> getStringValue(@Nonnull String category, @Nullable String key);

    Optional<Boolean> getBoolValue(@Nonnull String category, @Nullable String key);

    Optional<LocalDate> getDateValue(@Nonnull String category, @Nullable String key);

    Optional<JsonNode> getJsonValue(@Nonnull String category, @Nullable String key);
}
