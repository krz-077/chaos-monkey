package io.orefice.chaos.monkey.service.random;

import java.util.Random;

public class JdkRandomService implements RandomService {

    private final Random random;

    public JdkRandomService(Random random) {
        this.random = random;
    }
    @Override
    public int randomIndex(int range) {
        return random.nextInt(range);
    }
}