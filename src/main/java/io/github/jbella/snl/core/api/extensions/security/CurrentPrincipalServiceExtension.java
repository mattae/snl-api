package io.github.jbella.snl.core.api.extensions.security;

import org.pf4j.ExtensionPoint;

import java.util.List;
import java.util.Optional;

public interface CurrentPrincipalServiceExtension extends ExtensionPoint {
    Optional<String> getPrincipal();

    List<String> grantedAuthorities();
}
