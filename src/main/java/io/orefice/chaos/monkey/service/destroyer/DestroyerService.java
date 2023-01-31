package io.orefice.chaos.monkey.service.destroyer;

import io.orefice.chaos.monkey.model.PodParameters;

import java.util.Optional;

public interface DestroyerService {
    Optional<String> findAndDelete(PodParameters podParameters);
}
