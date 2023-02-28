package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.blazebit.persistence.view.PreRemove;
import com.blazebit.persistence.view.*;
import io.github.jbella.snl.core.api.config.AuditEntityListener;
import io.github.jbella.snl.core.api.id.UUIDV7;
import io.github.jbella.snl.core.api.id.UUIDV7Generator;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Where(clause = "archived = false")
@Getter
@Setter
@Entity
@Table(name = "party_addresses")
@SQLDelete(sql = "update party_addresses set archived = true, last_modified_date = current_timestamp where id = ?", check = ResultCheckStyle.COUNT)
@EntityListeners(AuditEntityListener.class)
public class Address {
    @Id
    @UUIDV7
    @EqualsAndHashCode.Include
    UUID id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotNull
    private String line1;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String line2;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotNull
    private String city;

    @EqualsAndHashCode.Include
    @ToString.Include
    @NotNull
    private String state;

    @ToString.Include
    private String postalCode;

    @EqualsAndHashCode.Include
    private String addressType;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Party party;

    private Boolean archived = false;

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @PreUpdate
    public void update() {
        lastModifiedDate = LocalDateTime.now();
    }

    @EntityView(Address.class)
    public interface View {
        @IdMapping
        UUID getId();

        String getLine1();

        String getLine2();

        String getCity();

        String getState();

        String getPostalCode();

        String getAddressType();
    }

    @EntityView(Address.class)
    @CreatableEntityView
    @UpdatableEntityView
    public interface AddressView extends Address.View {
        void setId(UUID id);

        void setLine1(String line1);

        void setLine2(String line2);

        void setCity(String city);

        void setState(String state);

        void setPostalCode(String postalCode);

        void setAddressType(String addressType);

        Party.View getParty();

        void setParty(Party.View party);

        @PostCreate
        default void init() {
            setAddressType("Residential");
        }

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
