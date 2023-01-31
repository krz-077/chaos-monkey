package io.orefice.chaos.monkey.model;

import java.util.Objects;

public class PodParameters {
    private final String namespace;
    private final String label;

    public PodParameters(String namespace, String label) {
        this.namespace = namespace;
        this.label = label;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PodParameters that = (PodParameters) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, label);
    }
}
