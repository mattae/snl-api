package io.github.jbella.snl.core.api.config;

import io.github.jbella.snl.core.api.domain.AuditableEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

import static io.github.jbella.snl.core.api.config.AuditViewListenersConfiguration.getPrincipal;

public class AuditEntityListener {

    @PrePersist
    private void beforeAnyPersist(Object entity) {
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
    private void beforeAnyUpdate(Object entity) {
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedBy(getPrincipal());
            auditable.setLastModifiedDate(date);
        }
    }

    @PreRemove
    private void beforeAnyRemove(Object entity) {
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(getPrincipal());
        }
    }
}