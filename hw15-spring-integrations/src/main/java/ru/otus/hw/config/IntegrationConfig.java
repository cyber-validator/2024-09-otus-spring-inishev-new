package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.model.Body;
import ru.otus.hw.model.BodyType;
import ru.otus.hw.model.Engine;
import ru.otus.hw.model.EngineType;
import ru.otus.hw.model.Equipment;
import ru.otus.hw.model.EquipmentType;
import ru.otus.hw.model.Vehicle;

import java.util.Random;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> inputChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> outputChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow flow() {
        return IntegrationFlow
                .from(inputChannel())

                .<Body>handle((body, headers)
                        -> {
                    int num = new Random().nextInt(BodyType.values().length);
                    body.setBodyType(BodyType.values()[num]);
                    return body;
                })

                .<Body, Vehicle>transform(b
                        -> {
                    Vehicle v = new Vehicle();
                    v.setBody(b);
                    return v;
                })

                .<Vehicle>handle((vehicle, headers) -> {
                    vehicle.setVin("XRTYB" + new Random().nextInt());

                    int num = new Random().nextInt(EngineType.values().length);
                    Engine en = new Engine();
                    en.setEngineType(EngineType.values()[num]);
                    vehicle.setEngine(en);

                    num = new Random().nextInt(EquipmentType.values().length);
                    Equipment eq = new Equipment();
                    eq.setEquipmentType(EquipmentType.values()[num]);
                    vehicle.setEquipment(eq);
                    return vehicle;
                })

                .channel(outputChannel())
                .get();
    }

}
