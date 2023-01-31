package io.orefice.chaos.monkey.application.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "destroyer.configuration")
@ConstructorBinding
public class DestroyerConfig {
    private final String label;
    private final String namespace;

    public DestroyerConfig(String label, String namespace) {
        this.label = label;
        this.namespace = namespace;
    }

    public String getLabel() {
        return label;
    }

    public String getNamespace() {
        return namespace;
    }
}
