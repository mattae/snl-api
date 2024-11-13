package io.github.mattae.snl.core.api.extensions.security;

import io.github.mattae.snl.core.api.extensions.OrderedExtension;

import java.util.List;
import java.util.Optional;

public interface CurrentPrincipalServiceExtension extends OrderedExtension {
    Optional<String> getPrincipal();

    List<String> grantedAuthorities();
}
