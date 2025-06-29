package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Body;
import ru.otus.hw.model.BodyType;
import ru.otus.hw.service.BodySupplierService;
import ru.otus.hw.service.RandomNumberGeneratorService;

@Service
@RequiredArgsConstructor
public class RandomBodySupplierService implements BodySupplierService {

    private final RandomNumberGeneratorService numberGeneratorService;

    @Override
    public Body getBody(String vin) {
        int bound = BodyType.values().length;
        int index = numberGeneratorService.generateBoundedRandom(bound);
        BodyType bodyType = BodyType.values()[index];
        return new Body(vin, bodyType);
    }

}
