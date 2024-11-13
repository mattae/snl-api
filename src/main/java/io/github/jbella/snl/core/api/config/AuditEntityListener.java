package io.github.jbella.snl.core.api.config;

import io.github.jbella.snl.core.api.domain.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static io.github.jbella.snl.core.api.config.AuditViewListenersConfiguration.getPrincipal;

@Slf4j
public class AuditEntityListener {

    @PrePersist
    public void beforeAnyPersist(Object entity) {
        log.debug("beforeAnyPersist {}", entity);
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
        log.info("Before remove entity: {}, {}", entity, AuditableEntity.class.isAssignableFrom(entity.getClass()));
        if (AuditableEntity.class.isAssignableFrom(entity.getClass())) {
            AuditableEntity auditable = (AuditableEntity) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(getPrincipal());
        }
    }
}