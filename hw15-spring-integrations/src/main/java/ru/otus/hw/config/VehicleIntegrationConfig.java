package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.service.VehicleAssembleService;

@Configuration
public class VehicleIntegrationConfig {

    public static final int QUEUE_CAPACITY = 1_000;

    @Bean
    public MessageChannelSpec<?, ?> assembleVehicleInputChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY);
    }

    @Bean
    public MessageChannelSpec<?, ?> assembleVehicleOutputChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow vehicleAssemblyFlow(VehicleAssembleService vehicleAssembleService) {
        return IntegrationFlow
                .from(assembleVehicleInputChannel())
                .transform(vehicleAssembleService::addBody)
                .transform(vehicleAssembleService::addEngine)
                .transform(vehicleAssembleService::addEquipment)
                .channel(assembleVehicleOutputChannel())
                .get();
    }

}
