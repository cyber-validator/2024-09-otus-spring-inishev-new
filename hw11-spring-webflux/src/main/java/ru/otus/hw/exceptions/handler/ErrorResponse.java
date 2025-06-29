package ru.otus.hw.exceptions.handler;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {

    private String message;

    private String url;

    private String method;

    private Instant timestamp = Instant.now();

}
