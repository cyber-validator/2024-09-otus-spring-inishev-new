package ru.otus.hw.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle {

    private String vin;

    private Body body;

    private Engine engine;

    private Equipment equipment;

    @Override
    public String toString() {
        return "Vehicle [" +
                "VIN=" + vin +
                ", body=" + body.getBodyType() +
                ", engine=" + engine.getEngineType() +
                ", equipment=" + equipment.getEquipmentType() +
                ']';
    }

}
