package ru.otus.hw.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Body;
import ru.otus.hw.model.Vehicle;

@MessagingGateway
public interface IntegrationGateway {

    @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
    Vehicle process(Body template);

}
