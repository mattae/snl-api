package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private String system;

    private String lang;

    private String description;

    @Column(name = "_order")
    private Integer order = 1;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Set<Value> values = new HashSet<>();

    @ManyToOne
    private Plugin plugin;

    @EntityView(ValueSet.class)
    @CreatableEntityView
    public interface BaseView {
        @IdMapping
        Long getId();

        String getSystem();

        void setSystem(String system);

        String getLang();

        void setLang(String lang);

        String getDescription();

        void setDescription(String description);

        Integer getOrder();

        void setOrder(Integer order);

        PluginView getPlugin();

        void setPlugin(PluginView plugin);

        @MappingSingular
        @NotEmpty
        Set<Value> getValues();

        void setValues(Set<Value> values);
    }

    @UpdatableEntityView
    @EntityView(ValueSet.class)
    public interface UpdateView extends BaseView {
        void setId(Long id);
    }

    public record DisplayView(String display) {
    }

    @EntityView(ValueSet.class)
    public record ValueView(@MappingSingular Set<Value> values) {
    }

    @EntityView(ValueSet.class)
    public interface SystemView {
        @IdMapping
        Long getId();

        String getSystem();

        String getDescription();

        Integer getOrder();
    }

    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Value {

        private Boolean active = true;

        @EqualsAndHashCode.Include
        @NotNull
        private String code;

        @NotNull
        private String display;
    }

    @EntityView(Plugin.class)
    public interface PluginView {
        @IdMapping
        UUID getId();
    }
}
