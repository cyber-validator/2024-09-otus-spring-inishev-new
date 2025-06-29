package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import ru.otus.hw.service.VehicleAssembleService;

@Configuration
public class VehicleIntegrationConfig {

    public static final int QUEUE_CAPACITY = 1_000;

    @Bean
    public MessageChannelSpec<?, ?> assembleVehicleInputChannel() {
        return MessageChannels.queue(QUEUE_CAPACITY);
    }

//    @Bean
//    public MessageChannel bodyChannel() {
//        return MessageChannels.direct().getObject();
//    }
//
//    @Bean
//    public MessageChannel engineChannel() {
//        return MessageChannels.direct().getObject();
//    }
//
//    @Bean
//    public MessageChannel equipmentChannel() {
//        return MessageChannels.direct().getObject();
//    }

    @Bean
    public MessageChannelSpec<?, ?> assembleVehicleOutputChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    public IntegrationFlow vehicleAssemblyFlow(VehicleAssembleService vehicleAssembleService) {
        return IntegrationFlow
                .from(assembleVehicleInputChannel())
//                .channel(bodyChannel())
                .transform(vehicleAssembleService::addBody)
//                .channel(engineChannel())
                .transform(vehicleAssembleService::addEngine)
//                .channel(equipmentChannel())
                .transform(vehicleAssembleService::addEquipment)
                .channel(assembleVehicleOutputChannel())
                .get();
    }

}
