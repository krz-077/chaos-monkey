package io.orefice.chaos.monkey.service;

import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.destroyer.DefaultDestroyerService;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import io.orefice.chaos.monkey.service.kubernetes.KubernetesApi;
import io.orefice.chaos.monkey.service.random.RandomService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultDestroyerServiceTest {

    private final KubernetesApi kubernetesApi = mock(KubernetesApi.class);
    private final RandomService randomService = mock(RandomService.class);
    private final DestroyerService underTest = new DefaultDestroyerService(kubernetesApi, randomService);

    @Test
    void shouldReturnDeletedPodWhenPodsArePresent() {
        when(kubernetesApi.getPodListBy(new PodParameters("testing", "aLabel")))
                .thenReturn(List.of("pod1", "pod2"));
        when(kubernetesApi.deletePod("pod1", "testing"))
                .thenReturn(Optional.of("pod1"));

        when(randomService.randomIndex(1)).thenReturn(0);

        assertThat(underTest.findAndDelete(new PodParameters("testing", "aLabel"))).isEqualTo(Optional.of("pod1"));
    }

    @Test
    void shouldReturnEmptyWhenPodsAreNotPresent() {
        when(kubernetesApi.getPodListBy(new PodParameters("testing", "aLabel")))
                .thenReturn(emptyList());

        assertThat(underTest.findAndDelete(new PodParameters("testing", "aLabel"))).isEqualTo(Optional.empty());
        verifyNoInteractions(randomService);
    }
}