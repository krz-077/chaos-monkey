package io.orefice.chaos.monkey.service.kubernetes;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.orefice.chaos.monkey.model.PodParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OfficialKubernetesApi implements KubernetesApi {
    private final CoreV1Api api;
    private final Logger logger = LoggerFactory.getLogger(OfficialKubernetesApi.class);

    public OfficialKubernetesApi(CoreV1Api api) {
        this.api = api;
    }

    @Override
    public Optional<String> deletePod(String podName, String namespace) {
        try {
            String name = api.deleteNamespacedPod(podName,
                    namespace,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null).getMetadata().getName();

            return Optional.ofNullable(name);
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                logger.debug("pod not found: ", e);
                return Optional.empty();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getPodListBy(PodParameters podParameters) {
        try {
            return api.listNamespacedPod(podParameters.getNamespace(),
                            null,
                            null,
                            null,
                            null,
                            "app=" + podParameters.getLabel(),
                            null,
                            null,
                            null,
                            null,
                            null)
                    .getItems().stream()
                    .map(v1Pod -> v1Pod.getMetadata().getName())
                    .collect(Collectors.toList());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}