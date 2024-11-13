package io.github.mattae.snl.core.api.domain;

import io.github.mattae.snl.core.api.id.UUIDV7;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    private String category;

    private String key;

    @NotNull
    private String value;
}
