package io.github.jbella.snl.core.api.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Plugin extends PluginBase implements Serializable {
}
