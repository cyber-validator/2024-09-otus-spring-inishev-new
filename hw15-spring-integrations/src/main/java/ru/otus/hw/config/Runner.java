package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.model.Body;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final IntegrationGateway gateway;

    @Override
    public void run(String... args) {
        for (byte i = 0; ; i++) {
            sleep(2_000);
            var res = gateway.process(new Body());
            log.info("Received data #{}: {}", i, res);
        }
    }

    private void sleep(int delay) {
        try {
            log.info("=> SLEEP");
            Thread.sleep(delay);
            log.info("=> WAKE UP");
        } catch (InterruptedException ignored) {
        }
    }

}
