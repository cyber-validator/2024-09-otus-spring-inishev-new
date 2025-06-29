package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Equipment;
import ru.otus.hw.model.EquipmentType;
import ru.otus.hw.service.EquipmentSupplierService;
import ru.otus.hw.service.RandomNumberGeneratorService;

@Service
@RequiredArgsConstructor
public class RandomEquipmentSupplierService implements EquipmentSupplierService {

    private final RandomNumberGeneratorService numberGeneratorService;

    @Override
    public Equipment getEquipment() {
        int bound = EquipmentType.values().length;
        int index = numberGeneratorService.generateBoundedRandom(bound);
        EquipmentType equipmentType = EquipmentType.values()[index];
        return new Equipment(equipmentType);
    }

}
