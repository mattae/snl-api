package io.github.mattae.snl.core.api.domain;

import com.blazebit.persistence.view.EntityView;
import io.github.mattae.snl.core.api.id.UUIDV7;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "fw_icon_set")
public class IconSet {
    @Id
    @UUIDV7
    @EqualsAndHashCode.Include
    private UUID id;

    private String namespace;

    private String svg;

    @ManyToOne(fetch = FetchType.LAZY)
    private Plugin plugin;

    @EntityView(IconSet.class)
    public interface View {
        String getNamespace();

        String getSvg();
    }
}
