package io.github.mattae.snl.core.api.config;

import io.github.mattae.snl.core.api.domain.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

import static io.github.mattae.snl.core.api.config.AuditViewListenersConfiguration.getPrincipal;

public class AuditEntityListener {

    @PrePersist
    public void beforeAnyPersist(Object entity) {
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setCreatedDate(date);
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(getPrincipal());
            auditable.setCreatedBy(getPrincipal());
        }
    }

    @PreUpdate
    public void beforeAnyUpdate(Object entity) {
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedBy(getPrincipal());
            auditable.setLastModifiedDate(date);
        }
    }

    @PreRemove
    public void beforeAnyRemove(Object entity) {
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(getPrincipal());
        }
    }
}
