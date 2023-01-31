package io.orefice.chaos.monkey.service;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.kubernetes.OfficialKubernetesApi;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfficialKubernetesApiTest {

    private final CoreV1Api coreV1Api = mock(CoreV1Api.class);
    private final OfficialKubernetesApi underTest = new OfficialKubernetesApi(coreV1Api);

    @Test
    void shouldReturnListIfSomePodContainsTheLabel() throws ApiException {
        when(coreV1Api.listNamespacedPod("aNamespace",
                null,
                null,
                null,
                null,
                "app=aLabel",
                null,
                null,
                null,
                null,
                null)).thenReturn(aPodList("aPod"));

        assertThat(underTest.getPodListBy(new PodParameters("aNamespace", "aLabel"))).contains("aPod");
    }

    @Test
    void shouldReturnEmpty() throws ApiException {
        V1PodList v1PodList = new V1PodList();
        v1PodList.setItems(emptyList());

        when(coreV1Api.listNamespacedPod("aNamespace",
                null,
                null,
                null,
                null,
                "app=aLabel",
                null,
                null,
                null,
                null,
                null)).thenReturn(v1PodList);

        assertThat(underTest.getPodListBy(new PodParameters("aNamespace", "aLabel"))).isEmpty();
    }

    @Test
    void shouldReturnRuntimeExceptionWhenGettingPodsIfSomethingGoesWrong() throws ApiException {
        when(coreV1Api.listNamespacedPod("aNamespace",
                null,
                null,
                null,
                null,
                "app=aLabel",
                null,
                null,
                null,
                null,
                null)).thenThrow(new ApiException());

        assertThrows(RuntimeException.class, () -> underTest.getPodListBy(new PodParameters("aNamespace", "aLabel")));
    }

    @Test
    void shouldDeletePodIfFoundIt() throws ApiException {
        V1Pod v1Pod = aPod("aPod");
        when(coreV1Api.deleteNamespacedPod("aPod",
                "aNamespace",
                null,
                null,
                null,
                null,
                null,
                null)).thenReturn(v1Pod);

        assertThat(underTest.deletePod("aPod", "aNamespace")).isEqualTo(Optional.of("aPod"));
    }

    @Test
    void shouldReturnEmptyIfPodNotFound() throws ApiException {
        when(coreV1Api.deleteNamespacedPod("aPodNotPresent",
                "aNamespace",
                null,
                null,
                null,
                null,
                null,
                null))
                .thenThrow(new ApiException(
                        null,
                        new RuntimeException(),
                        404,
                        null,
                        null
                ));

        assertThat(underTest.deletePod("aPodNotPresent", "aNamespace")).isEmpty();
    }

    @Test
    void shouldReturnRuntimeExceptionWhenDeletingIfSomethingGoesWrong() throws ApiException {
        when(coreV1Api.deleteNamespacedPod("somethingStrange",
                "aNamespace",
                null,
                null,
                null,
                null,
                null,
                null))
                .thenThrow(new ApiException(
                        null,
                        new RuntimeException(),
                        500,
                        null,
                        null
                ));

        assertThrows(RuntimeException.class, () -> underTest.deletePod("somethingStrange", "aNamespace"));
    }

    private V1Pod aPod(String aPod) {
        V1Pod v1Pod = new V1Pod();
        V1ObjectMeta metadata = new V1ObjectMeta();
        metadata.setName(aPod);
        v1Pod.setMetadata(metadata);
        return v1Pod;
    }

    private V1PodList aPodList(String aPod) {
        V1PodList v1PodList = new V1PodList();
        V1Pod v1Pod = aPod(aPod);

        v1PodList.setItems(List.of(v1Pod));
        return v1PodList;
    }
}