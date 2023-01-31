package io.orefice.chaos.monkey.application.controller;

import io.orefice.chaos.monkey.model.PodParameters;
import io.orefice.chaos.monkey.service.destroyer.DestroyerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DestroyerController {
    private final DestroyerService destroyerService;

    public DestroyerController(DestroyerService destroyerService) {
        this.destroyerService = destroyerService;
    }

    @GetMapping("/destroy")
    public ResponseEntity<String> destroy(@RequestParam String namespace, @RequestParam String label) {
        return destroyerService.findAndDelete(new PodParameters(namespace, label))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
