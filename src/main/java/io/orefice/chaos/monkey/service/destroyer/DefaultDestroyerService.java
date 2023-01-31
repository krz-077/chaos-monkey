package io.orefice.chaos.monkey.service.destroyer;

import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.kubernetes.KubernetesApi;
import io.orefice.chaos.monkey.service.kubernetes.OfficialKubernetesApi;
import io.orefice.chaos.monkey.service.random.JdkRandomService;
import io.orefice.chaos.monkey.service.random.RandomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Optional;

public class DefaultDestroyerService implements DestroyerService {
    final Logger logger = LoggerFactory.getLogger(DefaultDestroyerService.class);
    private final RandomService randomService;
    private final KubernetesApi kubernetesApi;

    public DefaultDestroyerService(KubernetesApi kubernetesApi, RandomService randomService) {
        this.randomService = randomService;
        this.kubernetesApi = kubernetesApi;
    }

    @Override
    public Optional<String> findAndDelete(PodParameters podParameters) {
        var pods = kubernetesApi.getPodListBy(podParameters);

        if (pods.isEmpty()) {
            logger.info("sorry, no pods are present in namespace: " + podParameters.getNamespace());
            return Optional.empty();
        }

        int randomIndex = randomService.randomIndex(pods.size() - 1);

        var deletedPod = kubernetesApi.deletePod(pods.get(randomIndex), podParameters.getNamespace());

        deletedPod.ifPresentOrElse(
                pod -> logger.info("pod deleted: " + pod),
                () -> logger.error("something goes wrong")
        );

        return deletedPod;
    }

/*
    public static void main(String[] args) {

        DefaultDestroyerService defaultDestroyerService = new DefaultDestroyerService(new OfficialKubernetesApi(new CoreV1Api()), new JdkRandomService(new SecureRandom()));

        defaultDestroyerService.findAndDelete(new PodParameters(args[0], args[1]));
    }
*/
}

