package ru.otus.hw.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.hw.service.RandomNumberGeneratorService;

import java.security.SecureRandom;

@Service
public class RandomNumberGeneratorServiceImpl implements RandomNumberGeneratorService {

    private final SecureRandom random = new SecureRandom();

    @Override
    public int generateRandom() {
        return random.nextInt();
    }

    @Override
    public int generateBoundedRandom(int bound) {
        return random.nextInt(bound);
    }

}
