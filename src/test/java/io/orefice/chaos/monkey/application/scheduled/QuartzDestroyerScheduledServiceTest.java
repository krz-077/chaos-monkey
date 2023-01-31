package io.orefice.chaos.monkey.application.scheduled;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.orefice.chaos.monkey.application.helpers.IntegrationTest;
import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

class QuartzDestroyerScheduledServiceTest extends IntegrationTest {

    @MockBean
    private CoreV1Api api;

    @MockBean
    private DestroyerService destroyerService;

    @Test
    void shouldCallDestroyServiceWithParametersWithSchedule() {
        when(destroyerService.findAndDelete(new PodParameters("aCronNamespace", "aCronLabel")))
                .thenReturn(Optional.of("something"));
        await()
                .until(() -> destroyerService.findAndDelete(new PodParameters("aCronNamespace", "aCronLabel")),
                        equalTo(Optional.of("something")));
    }

}