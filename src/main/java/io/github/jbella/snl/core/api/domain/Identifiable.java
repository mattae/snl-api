package io.github.jbella.snl.core.api.domain;

import io.github.jbella.snl.core.api.id.UUIDV7Generator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class Identifiable {
    @Id
    @GeneratedValue(generator = UUIDV7Generator.GENERATOR)
    @GenericGenerator(name = UUIDV7Generator.GENERATOR, strategy = "io.github.jbella.snl.core.api.id.UUIDV7Generator")
    protected UUID id;
}
