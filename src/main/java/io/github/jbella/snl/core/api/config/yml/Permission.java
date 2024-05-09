package io.github.jbella.snl.core.api.config.yml;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private String name;
    private String description;
    private String group;
}
