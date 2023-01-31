package io.orefice.chaos.monkey.service;

import io.orefice.chaos.monkey.service.random.JdkRandomService;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class JdkRandomServiceTest {

    private final JdkRandomService underTest = new JdkRandomService(new SecureRandom());

    @Test
    void shouldReturnNumberUnderTheRange() {
        assertThat(underTest.randomIndex(5) <=5).isTrue();
    }

    @Test
    void shouldThrowExceptionIfNumberIsNotPositive() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> underTest.randomIndex(0));
    }
}