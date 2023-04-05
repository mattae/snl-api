package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public interface AuditableView {
    @JsonIgnore
    LocalDateTime getLastModifiedDate();

    void setLastModifiedDate(LocalDateTime date);

    @JsonIgnore
    LocalDateTime getCreatedDate();

    void setCreatedDate(LocalDateTime date);

    @JsonIgnore
    String getCreatedBy();

    void setCreatedBy(String createdBy);

    @JsonIgnore
    String getLastModifiedBy();

    void setLastModifiedBy(String lastModifiedBy);

    @JsonIgnore
    Boolean getArchived();

    void setArchived(Boolean archived);

    @PrePersist
    default void prePersist() {
        setArchived(false);
    }
}
