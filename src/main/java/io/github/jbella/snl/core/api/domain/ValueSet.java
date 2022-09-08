package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
public class ValueSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @NotNull
    private String code;

    @NotNull
    private String display;

    @EqualsAndHashCode.Include
    @NotNull
    private String system;

    private Boolean active = true;

    private String lang;

    @ManyToOne
    private Plugin plugin;

    @EntityView(ValueSet.class)
    @CreatableEntityView
    public interface BaseView {
        @IdMapping
        Long getId();

        String getSystem();

        void setSystem(String provider);

        String getCode();

        void setCode(String code);

        String getDisplay();

        void setDisplay(String display);

        Boolean getActive();

        void setActive(Boolean active);

        String getLang();

        void setLang(String lang);

        @PostCreate
        default void init() {
            setActive(true);
        }
    }

    @UpdatableEntityView
    @EntityView(ValueSet.class)
    public interface UpdateView extends BaseView {
        void setId(Long id);
    }

    @EntityView(ValueSet.class)
    public interface DisplayView {
        String getDisplay();
    }
}
