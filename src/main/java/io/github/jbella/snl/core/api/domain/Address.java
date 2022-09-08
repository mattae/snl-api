package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.blazebit.persistence.view.PreRemove;
import com.blazebit.persistence.view.*;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.github.jbella.snl.core.api.config.AuditEntityListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
        @TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class),
        @TypeDef(name = "json-node", typeClass = JsonNodeStringType.class),
})
public class Address {
    @Id
    @GeneratedValue
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
