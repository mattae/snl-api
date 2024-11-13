package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.EntityView;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "fw_translation")
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @NotNull
    private JsonNode data;

    @EntityView(Translation.class)
    public interface View {
        Long getId();

        Integer getOrder();

        JsonNode getData();
    }
}
