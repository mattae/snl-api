package io.github.jbella.snl.core.api.domain;

import io.github.jbella.snl.core.api.id.UUIDV7Generator;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of = "name")
public class PluginBase implements Persistable<UUID> {
    @Id
    @GeneratedValue(generator = UUIDV7Generator.GENERATOR)
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String name;

    private String version;

    private Boolean started = false;

    private Boolean enabled = true;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
