package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.MappingSingular;
import com.blazebit.persistence.view.UpdatableEntityView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotNull
    @EqualsAndHashCode.Include
    private String category;

    @Column(name = "_order")
    private Integer order = 1;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private Set<Data> data;

    @ManyToOne
    private Plugin plugin;

    @EntityView(Configuration.class)
    @UpdatableEntityView
    public interface View {
        @IdMapping
        @NotNull
        Long getId();

        void setId(Long id);

        @NotNull
        String getCategory();

        void setCategory(String category);

        Integer getOrder();

        void setOrder(Integer order);

        PluginView getPlugin();

        @NotEmpty
        @MappingSingular
        Set<Data> getData();

        void setData(Set<Data> configurations);
    }

    @Getter
    @Setter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Data {
        @NotNull
        @EqualsAndHashCode.Include
        private String key;

        @NotNull
        private String value;

        @NotNull
        private Type type;

        public enum Type {
            string, numeric, bool, date
        }
    }

    @EntityView(Plugin.class)
    public interface PluginView {
        @IdMapping
        UUID getId();
    }

    @EntityView(ValueSet.class)
    public interface ConfigurationPluginView {
        @IdMapping
        Long getId();

        PluginView getPlugin();
    }
}
