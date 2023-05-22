package io.github.jbella.snl.core.api.domain;

import io.github.jbella.snl.core.api.id.UUIDV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Preference {
    @Id
    @UUIDV7
    private Long id;

    @NotNull
    private String category;

    private String key;

    @NotNull
    private String value;
}
