package io.github.mattae.snl.core.api.domain;

import java.time.LocalDateTime;

public interface Auditable<U> {
    U getCreatedBy();

    void setCreatedBy(U createdBy);

    LocalDateTime getCreatedDate();

    void setCreatedDate(LocalDateTime createdDate);

    U getLastModifiedBy();

    void setLastModifiedBy(U lastModifiedBy);

    LocalDateTime getLastModifiedDate();

    void setLastModifiedDate(LocalDateTime lastModifiedDate);
}
