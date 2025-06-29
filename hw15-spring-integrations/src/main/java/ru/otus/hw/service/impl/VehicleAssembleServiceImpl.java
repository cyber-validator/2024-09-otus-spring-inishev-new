package ru.otus.hw.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Engine;
import ru.otus.hw.model.Equipment;
import ru.otus.hw.model.Vehicle;
import ru.otus.hw.service.BodySupplierService;
import ru.otus.hw.service.EngineSupplierService;
import ru.otus.hw.service.EquipmentSupplierService;
import ru.otus.hw.service.VehicleAssembleService;

@Service
@RequiredArgsConstructor
public class VehicleAssembleServiceImpl implements VehicleAssembleService {

    private final BodySupplierService bodySupplierService;

    private final EngineSupplierService engineSupplierService;

    private final EquipmentSupplierService equipmentSupplierService;

    @Override
    public Vehicle addBody(String vin) {
        return new Vehicle(bodySupplierService.getBody(vin));
    }

    @Override
    public Vehicle addEngine(Vehicle vehicle) {
        Engine engine = engineSupplierService.getEngine();
        vehicle.setEngine(engine);
        return vehicle;
    }

    @Override
    public Vehicle addEquipment(Vehicle vehicle) {
        Equipment equipment = equipmentSupplierService.getEquipment();
        vehicle.setEquipment(equipment);
        return vehicle;
    }

}
