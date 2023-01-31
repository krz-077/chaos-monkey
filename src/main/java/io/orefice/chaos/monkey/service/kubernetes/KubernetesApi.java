package io.orefice.chaos.monkey.service.kubernetes;

import io.orefice.chaos.monkey.model.PodParameters;

import java.util.List;
import java.util.Optional;

public interface KubernetesApi {

    Optional<String> deletePod(String podName, String namespace);

    List<String> getPodListBy(PodParameters podParameters);
}
