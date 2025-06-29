package ru.otus.hw.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import ru.otus.hw.model.Vehicle;

@MessagingGateway
public interface IntegrationGateway {

    @Gateway(requestChannel = "assembleVehicleInputChannel", replyChannel = "assembleVehicleOutputChannel")
    Vehicle produceVehicle(@Payload String vehicleIdentificationNumber);

}
