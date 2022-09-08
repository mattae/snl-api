package io.github.jbella.snl.core.api.config.yml;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Role {
    Set<Permission> permissions;
    private String name;
    private String authority;
    private String description;
}
