package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.CreatableEntityView;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.UpdatableEntityView;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Embeddable
@Getter
@Setter
public class Name {

    private String title;

    private String givenName;

    private String preferredGivenName;

    private String middleName;

    private String initials;

    private String familyName;

    private String preferredFamilyName;

    private String preferredName;

    private String honorific;

    private String salutation;

    @EntityView(Name.class)
    public interface View {
        String getTitle();

        @NotBlank
        String getGivenName();

        String getPreferredGivenName();

        String getMiddleName();

        String getInitials();

        @NotBlank
        String getFamilyName();
    }

    @EntityView(Name.class)
    @CreatableEntityView
    @UpdatableEntityView
    public interface NameView extends Name.View {

        void setTitle(String title);

        void setGivenName(String name);

        void setPreferredGivenName(String name);

        void setMiddleName(String name);

        void setInitials(String initials);

        void setFamilyName(String familyName);
    }
}
