package io.github.jbella.snl.core.api.domain;

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
}
