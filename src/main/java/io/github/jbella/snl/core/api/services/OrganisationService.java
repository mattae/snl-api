package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.Organisation;
import io.github.jbella.snl.core.api.services.util.PagedResult;

import java.util.Optional;
import java.util.UUID;

public interface OrganisationService {
    Optional<Organisation.CreateView> getById(UUID id);

    PagedResult<Organisation.View> list(String keyword, String type, Boolean active, int start, int pageSize);
}
