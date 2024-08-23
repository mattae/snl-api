package io.github.mattae.snl.core.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "fw_administrative_division")
public class AdministrativeDivision implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String code;

    @ManyToOne
    @JsonIgnore
    private Country country;

    @ManyToOne
    @JsonIgnore
    private AdministrativeDivision parent;
}
