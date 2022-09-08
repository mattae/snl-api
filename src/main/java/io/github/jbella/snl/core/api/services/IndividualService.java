package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.Individual;
import io.github.jbella.snl.core.api.services.util.PagedResult;

import java.util.Optional;
import java.util.UUID;

public interface IndividualService {
    Optional<Individual.CreateView> getById(UUID id);

    PagedResult<Individual.View> list(String keyword, Boolean active, int start, int pageSize);
}
