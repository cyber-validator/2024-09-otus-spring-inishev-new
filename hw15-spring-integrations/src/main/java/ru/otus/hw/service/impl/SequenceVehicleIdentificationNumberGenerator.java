package ru.otus.hw.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.hw.service.VehicleIdentificationNumberGenerator;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SequenceVehicleIdentificationNumberGenerator implements VehicleIdentificationNumberGenerator {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public String getVin() {
        int number = counter.getAndIncrement();
        return customFormat(number);
    }

    private String customFormat(int value) {
        DecimalFormat myFormatter = new DecimalFormat("CAR-00000000000");
        return myFormatter.format(value);
    }

}
