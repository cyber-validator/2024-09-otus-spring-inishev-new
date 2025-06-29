package ru.otus.hw.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehicle {

    private Body body;

    private Engine engine;

    private Equipment equipment;

    public Vehicle(Body body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Vehicle [" +
                "VIN=" + body.getVin() +
                ", body=" + body.getBodyType() +
                ", engine=" + engine.getEngineType() +
                ", equipment=" + equipment.getEquipmentType() +
                ']';
    }

}
