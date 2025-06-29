package ru.otus.hw;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.IntegrationGateway;
import ru.otus.hw.service.VehicleIdentificationNumberGenerator;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final IntegrationGateway gateway;

    private final VehicleIdentificationNumberGenerator vinGenerator;

    @Override
    public void run(String... args) {
        for (long i = 0; ; i++) {
            sleep(2_000);
            var res = gateway.produceVehicle(vinGenerator.getVin());
            log.info("Received data #{}: {}", i, res);
        }
    }

    private void sleep(int delay) {
        try {
            log.info("SLEEPING {} ms", delay);
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }

}
