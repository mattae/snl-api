package io.github.mattae.snl.core.api.domain;

import io.github.mattae.snl.core.api.id.UUIDV7;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "fw_preference")
public class Preference {
    @Id
    @UUIDV7
    private UUID id;

    @NotNull
    private String subject;

    @NotNull
    private String category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @NotEmpty
    private Set<Data> data;

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
}
