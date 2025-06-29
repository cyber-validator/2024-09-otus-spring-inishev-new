package ru.otus.hw.service;

import ru.otus.hw.model.Vehicle;

public interface VehicleAssembleService {

    Vehicle addBody(String vin);

    Vehicle addEngine(Vehicle vehicle);

    Vehicle addEquipment(Vehicle vehicle);

}
