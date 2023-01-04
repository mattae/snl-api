package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.PrePersist;
import com.blazebit.persistence.view.PreRemove;
import com.blazebit.persistence.view.PreUpdate;
import com.blazebit.persistence.view.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EqualsAndHashCode(of = "id")
@SQLDelete(sql = "update individual set archived = true, last_modified_date = current_timestamp where id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "archived = false")
@Getter
@Setter
public class Individual {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, cascade = jakarta.persistence.CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Party party;


    @ManyToOne(fetch = FetchType.LAZY)
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

    private Boolean archived = false;

    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @PreUpdate
    public void update() {
        lastModifiedDate = LocalDateTime.now();
    }


    @EntityView(Individual.class)
    public interface IdView {
        @IdMapping
        UUID getId();
    }

    @EntityView(Individual.class)
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
    @EntityView(Individual.class)
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

        @PreUpdate
        default void preUpdate() {
            setLastModifiedDate(LocalDateTime.now());
        }

        @PrePersist
        default void prePersist() {
            setArchived(false);
            setLastModifiedDate(LocalDateTime.now());
        }
    }

    @UpdatableEntityView
    @EntityView(Individual.class)
    public interface UpdateView extends CreateView {
        @IdMapping
        @NotNull
        UUID getId();

        void setId(UUID id);
    }
}
