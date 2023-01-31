package io.orefice.chaos.monkey.application.scheduled;

import io.orefice.chaos.monkey.application.configuration.properties.DestroyerConfig;
import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import org.springframework.scheduling.annotation.Scheduled;

public class QuartzDestroyerScheduledService {
    private final DestroyerService destroyerService;
    private final DestroyerConfig config;

    public QuartzDestroyerScheduledService(DestroyerConfig config, DestroyerService destroyerService) {
        this.config = config;
        this.destroyerService = destroyerService;
    }

    @Scheduled(fixedDelay = 30000)
    public void execute() {
        destroyerService.findAndDelete(new PodParameters(config.getNamespace(), config.getLabel()));
    }

}
