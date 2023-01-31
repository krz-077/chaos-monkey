package io.orefice.chaos.monkey.service;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.orefice.chaos.monkey.application.configuration.properties.DestroyerConfig;
import io.orefice.chaos.monkey.application.scheduled.QuartzDestroyerScheduledService;
import io.orefice.chaos.monkey.service.destroyer.DefaultDestroyerService;
import io.orefice.chaos.monkey.service.kubernetes.OfficialKubernetesApi;
import io.orefice.chaos.monkey.service.random.JdkRandomService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.SecureRandom;

import static io.kubernetes.client.openapi.Configuration.setDefaultApiClient;

class DestroyerQuartzScheduledServiceManualTest {

    @Test
    void runIt() throws IOException {
        ApiClient client = ClientBuilder.defaultClient();
        setDefaultApiClient(client);

        CoreV1Api api = new CoreV1Api();

        final OfficialKubernetesApi kubernetesApi = new OfficialKubernetesApi(api);
        final JdkRandomService randomService = new JdkRandomService(new SecureRandom());
        QuartzDestroyerScheduledService underTest = new QuartzDestroyerScheduledService(
                new DestroyerConfig("nginx-red", "testing"), new DefaultDestroyerService(kubernetesApi, randomService));

        underTest.execute();
    }

}