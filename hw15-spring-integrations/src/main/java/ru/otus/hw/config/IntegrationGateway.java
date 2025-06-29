package ru.otus.hw.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import ru.otus.hw.model.Body;
import ru.otus.hw.model.Vehicle;

@MessagingGateway
public interface IntegrationGateway {

    @Gateway(requestChannel = "assembleVehicleInputChannel", replyChannel = "assembleVehicleOutputChannel")
    Vehicle produceVehicle(@Payload String vehicleIdentificationNumber);

    /*@Gateway(requestChannel = "bodyChannel")
    Vehicle processBody(@Payload Body body);

    @Gateway(requestChannel = "engineChannel")
    Vehicle processEngine(@Payload Vehicle vehicle);

    @Gateway(requestChannel = "equipmentChannel")
    Vehicle processEquipment(@Payload Vehicle vehicle);*/

}
