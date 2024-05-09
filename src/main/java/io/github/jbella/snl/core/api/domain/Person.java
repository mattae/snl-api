package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.blazebit.persistence.view.PreRemove;
import com.blazebit.persistence.view.PreUpdate;
import com.blazebit.persistence.view.*;
import io.github.jbella.snl.core.api.id.UUIDV7;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EqualsAndHashCode(of = "id")
@SoftDelete(columnName = "archived")
@Getter
@Setter
@Table(name = "fw_person")
public class Person {
    @Id
    @UUIDV7
    private UUID id;

    @ManyToOne(optional = false, cascade = jakarta.persistence.CascadeType.REMOVE, fetch = FetchType.LAZY)
    @NotFound( action = NotFoundAction.EXCEPTION )
    private Party party;


    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound( action = NotFoundAction.EXCEPTION )
    private Organisation organisation;

    @Embedded
    private Name name;

    private String sex;

    @Email
    private String email;

    private String phone;

    private String photoUrl;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String countryOfBirth;

    private LocalDate dateOfDeath;

    private String placeOfDeath;

    private String countryOfDeath;

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @PreUpdate
    public void update() {
        lastModifiedDate = LocalDateTime.now();
    }


    @EntityView(Person.class)
    public interface IdView {
        @IdMapping
        UUID getId();
    }

    @EntityView(Person.class)
    public interface View extends IdView {

        String getSex();

        @Email
        String getEmail();

        String getPhone();

        String getPhotoUrl();

        String getPlaceOfBirth();

        LocalDate getDateOfBirth();

        String getCountryOfBirth();

        @NotNull
        Name.NameView getName();
    }

    @CreatableEntityView
    @EntityView(Person.class)
    public interface CreateView extends View {

        void setSex(String sex);

        void setEmail(String email);

        void setPhone(String phone);

        void setPhotoUrl(String url);

        void setPlaceOfBirth(String place);

        void setDateOfBirth(LocalDate dateOfBirth);

        void setCountryOfBirth(String country);

        @UpdatableMapping(orphanRemoval = true)
        @AllowUpdatableEntityViews
        @NotNull
        Party.PartyView getParty();

        void setParty(Party.PartyView party);

        Organisation.IdView getOrganisation();

        void setOrganisation(Organisation.IdView organisation);

        void setName(Name.NameView name);

        LocalDateTime getLastModifiedDate();

        void setLastModifiedDate(LocalDateTime date);

        @PreRemove
        default boolean preRemove() {
            setLastModifiedDate(LocalDateTime.now());
            return false;
        }

        @PrePersist
        default void prePersist() {
            getParty().setType("PERSON");
            setLastModifiedDate(LocalDateTime.now());
        }
    }

    @UpdatableEntityView
    @EntityView(Person.class)
    public interface UpdateView extends CreateView {
        @IdMapping
        @NotNull
        UUID getId();

        void setId(UUID id);

        @PreUpdate
        default void preUpdate() {
            setLastModifiedDate(LocalDateTime.now());
        }
    }
}
