package io.orefice.chaos.monkey.application.configuration;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.orefice.chaos.monkey.application.configuration.properties.DestroyerConfig;
import io.orefice.chaos.monkey.application.scheduled.QuartzDestroyerScheduledService;

import io.orefice.chaos.monkey.service.destroyer.DefaultDestroyerService;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import io.orefice.chaos.monkey.service.kubernetes.OfficialKubernetesApi;
import io.orefice.chaos.monkey.service.random.JdkRandomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.security.SecureRandom;

import static io.kubernetes.client.openapi.Configuration.*;

@Configuration
@EnableScheduling
public class DestroyerConfiguration {

    @Bean
    public QuartzDestroyerScheduledService quartzDestroyerScheduledService(DestroyerConfig destroyerConfig,
                                                      DestroyerService destroyerService) {

        return new QuartzDestroyerScheduledService(destroyerConfig, destroyerService);
    }

    @Bean
    public DestroyerService destroyerService(CoreV1Api api) {
        var kubernetesApi = new OfficialKubernetesApi(api);

        var randomService = new JdkRandomService(new SecureRandom());

        return new DefaultDestroyerService(kubernetesApi, randomService);
    }

    @Bean
    public CoreV1Api api() throws IOException {
        ApiClient client = ClientBuilder.cluster().build();
        setDefaultApiClient(client);

        return new CoreV1Api();
    }
}
