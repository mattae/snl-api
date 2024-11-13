package io.github.mattae.snl.core.api.config.yml;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private String name;
    private String description;
    private String group;
}
