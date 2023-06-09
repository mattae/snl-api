package io.github.jbella.snl.core.api.extensions;

import org.pf4j.ExtensionPoint;

public interface OrderedExtension extends ExtensionPoint {
    default int getOrder() {
        return 1;
    }
}
