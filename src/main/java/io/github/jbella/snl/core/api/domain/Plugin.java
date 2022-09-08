package io.github.jbella.snl.core.api.domain;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "plugin")
@EqualsAndHashCode(of = "name", callSuper = false)
public class Plugin implements Serializable, Persistable<UUID> {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Column(unique = true)
    private String name;

    private String version;

    private String logo;

    private Boolean started;

    private Boolean enabled;

    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] data;

    @Override
    public boolean isNew() {
        return id == null;
    }

    public Plugin copy() {
        Plugin plugin = new Plugin();
        BeanUtils.copyProperties(this, plugin, "data");
        return plugin;
    }

    @EntityView(Plugin.class)
    public interface View {
        @IdMapping
        UUID getId();

        String getName();
    }
}
