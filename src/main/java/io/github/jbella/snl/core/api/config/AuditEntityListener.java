package io.github.jbella.snl.core.api.config;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.springframework.data.jpa.domain.AbstractAuditable;

import java.time.LocalDateTime;

import static io.github.jbella.snl.core.api.config.AuditViewListenersConfiguration.getPrincipal;

public class AuditEntityListener {

    @PrePersist
    private void beforeAnyPersist(Object entity) {
        if (AbstractAuditable.class.isAssignableFrom(entity.getClass())) {
            AbstractAuditable<String, ?> auditable = (AbstractAuditable<String, ?>) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setCreatedDate(date);
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(getPrincipal());
            auditable.setCreatedBy(getPrincipal());
        }
    }

    @PreUpdate
    private void beforeAnyUpdate(Object entity) {
        if (AbstractAuditable.class.isAssignableFrom(entity.getClass())) {
            AbstractAuditable<String, ?> auditable = (AbstractAuditable<String, ?>) entity;
            LocalDateTime date = LocalDateTime.now();
            auditable.setLastModifiedBy(getPrincipal());
            auditable.setLastModifiedDate(date);
        }
    }

    @PreRemove
    private void beforeAnyRemove(Object entity) {
        AbstractAuditable<String, ?> auditable = (AbstractAuditable<String, ?>) entity;
        LocalDateTime date = LocalDateTime.now();
        auditable.setLastModifiedDate(date);
        auditable.setLastModifiedBy(getPrincipal());
    }
}