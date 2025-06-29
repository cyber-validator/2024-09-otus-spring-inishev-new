package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Engine;
import ru.otus.hw.model.EngineType;
import ru.otus.hw.service.EngineSupplierService;
import ru.otus.hw.service.RandomNumberGeneratorService;

@Service
@RequiredArgsConstructor
public class RandomEngineSupplierService implements EngineSupplierService {

    private final RandomNumberGeneratorService numberGeneratorService;

    @Override
    public Engine getEngine() {
        int bound = EngineType.values().length;
        int index = numberGeneratorService.generateBoundedRandom(bound);
        EngineType engineType = EngineType.values()[index];
        return new Engine(engineType);
    }

}
