package io.github.jbella.snl.core.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Setter
@MappedSuperclass
@Slf4j
public abstract class AuditableEntity implements Auditable<String> {
    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @Column(name = "created_date", nullable = true)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by", nullable = true)
    private String lastModifiedBy;

    @Column(name = "last_modified_date", nullable = true)
    private LocalDateTime lastModifiedDate;

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
}
