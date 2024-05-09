package io.github.jbella.snl.core.api.config;

import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.PrePersistListener;
import com.blazebit.persistence.view.PreRemoveListener;
import com.blazebit.persistence.view.PreUpdateListener;
import io.github.jbella.snl.core.api.domain.AuditableView;
import io.github.jbella.snl.core.api.extensions.security.CurrentPrincipalServiceExtension;
import jakarta.persistence.EntityManager;
import org.pf4j.PluginManager;

import java.time.LocalDateTime;
import java.util.Objects;

public class AuditViewListenersConfiguration<T> implements PrePersistListener<T>, PreUpdateListener<T>, PreRemoveListener<T> {

    public static String getPrincipal() {
        return ContextProvider.getBean(PluginManager.class)
                .getExtensions(CurrentPrincipalServiceExtension.class).stream().findFirst()
                .flatMap(CurrentPrincipalServiceExtension::getPrincipal).orElse("System");
    }

    @Override
    public void preUpdate(EntityViewManager entityViewManager, EntityManager entityManager, Object view) {
        if (AuditableView.class.isAssignableFrom(view.getClass())) {
            AuditableView auditable = (AuditableView) view;
            auditable.setLastModifiedDate(LocalDateTime.now());
            auditable.setLastModifiedBy(Objects.requireNonNullElse(getPrincipal(), "system"));
        }
    }

    @Override
    public void prePersist(EntityViewManager entityViewManager, EntityManager entityManager, Object view) {
        if (AuditableView.class.isAssignableFrom(view.getClass())) {
            AuditableView auditable = (AuditableView) view;
            LocalDateTime date = LocalDateTime.now();
            auditable.setCreatedDate(date);
            auditable.setLastModifiedDate(date);
            auditable.setLastModifiedBy(Objects.requireNonNullElse(getPrincipal(), "system"));
            auditable.setCreatedBy(Objects.requireNonNullElse(getPrincipal(), "system"));
        }
    }

    @Override
    public boolean preRemove(EntityViewManager entityViewManager, EntityManager entityManager, Object view) {
        if (AuditableView.class.isAssignableFrom(view.getClass())) {
            AuditableView auditable = (AuditableView) view;
            auditable.setLastModifiedDate(LocalDateTime.now());
            auditable.setLastModifiedBy(Objects.requireNonNullElse(getPrincipal(), "system"));
        }
        return false;
    }
}