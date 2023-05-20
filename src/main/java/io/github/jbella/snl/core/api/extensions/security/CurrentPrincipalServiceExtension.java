package io.github.jbella.snl.core.api.extensions.security;

import io.github.jbella.snl.core.api.extensions.OrderedExtension;
import org.pf4j.ExtensionPoint;

import java.util.List;
import java.util.Optional;

public interface CurrentPrincipalServiceExtension extends OrderedExtension {
    Optional<String> getPrincipal();

    List<String> grantedAuthorities();
}
