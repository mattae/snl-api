package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.UpdatableEntityView;
import io.github.jbella.snl.core.api.id.UUIDV7;
import io.github.jbella.snl.core.api.id.UUIDV7Generator;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fw_party_identifiers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Where(clause = "archived = false")
@Getter
@Setter
@SQLDelete(sql = "update fw_party_identifiers set archived = true, last_modified_date = current_timestamp where id = ?", check = ResultCheckStyle.COUNT)
public class Identifier {
    @Id
    @UUIDV7
    @EqualsAndHashCode.Include
    UUID id;

    @Column(name = "type", nullable = false)
    @EqualsAndHashCode.Include
    private String type;

    @Column(name = "value", nullable = false)
    @EqualsAndHashCode.Include
    private String value;

    @Column(name = "register", nullable = false)
    @EqualsAndHashCode.Include
    private String register;

    private String lifecycleStatus;

    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Party party;

    private Boolean archived = false;

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @PreUpdate
    public void update() {
        lastModifiedDate = LocalDateTime.now();
    }

    @EntityView(Identifier.class)
    public interface View {
        @IdMapping
        UUID getId();

        String getType();

        String getValue();

        String getRegister();

        LocalDateTime getFromDate();

        LocalDateTime getToDate();
    }

    @EntityView(Identifier.class)
    @CreatableEntityView
    @UpdatableEntityView
    public interface IdentifierView extends Identifier.View {
        void setId(UUID id);

        void setValue(String value);

        void setRegister(String register);

        void setType(String type);

        void setFromDate(LocalDateTime fromDate);

        void setToDate(LocalDateTime toDate);

        Boolean getArchived();

        void setArchived(Boolean archived);

        Party.View getParty();

        void setParty(Party.View party);

        LocalDateTime getLastModifiedDate();

        void setLastModifiedDate(LocalDateTime date);

        @com.blazebit.persistence.view.PreRemove
        default boolean preRemove() {
            setArchived(true);
            setLastModifiedDate(LocalDateTime.now());
            return false;
        }

        @com.blazebit.persistence.view.PreUpdate
        default void preUpdate() {
            setLastModifiedDate(LocalDateTime.now());
        }

        @com.blazebit.persistence.view.PrePersist
        default void prePersist() {
            setArchived(false);
            setLastModifiedDate(LocalDateTime.now());
        }
    }
}
