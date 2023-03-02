package io.github.jbella.snl.core.api.services;

import io.github.jbella.snl.core.api.domain.Person;
import io.github.jbella.snl.core.api.services.util.PagedResult;

import java.util.Optional;
import java.util.UUID;

public interface PersonService {
    Optional<Person.CreateView> getById(UUID id);

    PagedResult<Person.View> list(String keyword, Boolean active, int start, int pageSize);
}
