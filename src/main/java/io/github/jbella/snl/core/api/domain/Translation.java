package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.EntityView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Plugin plugin;

    @EqualsAndHashCode.Include
    @NotNull
    private String lang;

    @Column(name = "_order")
    private Integer order = 1;

    @Type(type = "jsonb")
    @NotNull
    private JsonNode data;

    @EntityView(Translation.class)
    public interface View {
        Long getId();

        Integer getOrder();

        JsonNode getData();
    }
}