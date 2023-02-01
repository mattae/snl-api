package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.blazebit.persistence.view.PreRemove;
import com.blazebit.persistence.view.*;
import com.blazebit.persistence.view.filter.EqualFilter;
import io.github.jbella.snl.core.api.id.UUIDV7Generator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@SQLDelete(sql = "update party set archived = true, last_modified_date = current_timestamp where id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "archived = false")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Party {
    @Id
    @GeneratedValue(generator = UUIDV7Generator.GENERATOR)
    private UUID id;

    private String type;

    private String legalType = "";

    private String displayName = "";

    @OneToMany(mappedBy = "party", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Identifier> identifiers = new HashSet<>();

    private Boolean archived = false;

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @PreUpdate
    public void update() {
        lastModifiedDate = LocalDateTime.now();
    }

    @EntityView(Party.class)
    public interface View {
        @IdMapping
        UUID getId();

        @AttributeFilter(EqualFilter.class)
        String getType();
    }

    @CreatableEntityView
    @UpdatableEntityView
    @EntityView(Party.class)
    public interface PartyView extends Party.View {
        void setType(String type);

        @UpdatableMapping(cascade = {com.blazebit.persistence.view.CascadeType.UPDATE, com.blazebit.persistence.view.CascadeType.PERSIST, com.blazebit.persistence.view.CascadeType.DELETE})
        @MappingInverse(removeStrategy = InverseRemoveStrategy.REMOVE)
        Set<Address.AddressView> getAddresses();

        void setAddresses(Set<Address.AddressView> addresses);

        @UpdatableMapping(cascade = {com.blazebit.persistence.view.CascadeType.UPDATE, com.blazebit.persistence.view.CascadeType.PERSIST, com.blazebit.persistence.view.CascadeType.DELETE})
        @MappingInverse(removeStrategy = InverseRemoveStrategy.REMOVE)
        Set<Identifier.IdentifierView> getIdentifiers();

        void setIdentifiers(Set<Identifier.IdentifierView> identifiers);

        Boolean getArchived();

        void setArchived(Boolean archived);

        LocalDateTime getLastModifiedDate();

        void setLastModifiedDate(LocalDateTime date);

        @PreRemove
        default boolean preRemove() {
            setArchived(true);
            setLastModifiedDate(LocalDateTime.now());
            return false;
        }

        @com.blazebit.persistence.view.PreUpdate
        default void preUpdate() {
            setLastModifiedDate(LocalDateTime.now());
        }

        @PrePersist
        default void prePersist() {
            setArchived(false);
            setLastModifiedDate(LocalDateTime.now());
        }
    }
}
