package ru.otus.hw.exceptions.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class SimpleGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> defaultErrorHandler(ServerWebExchange exchange, Exception e) {
        ErrorResponse rs = new ErrorResponse();
        String method = exchange.getRequest().getMethod().name();
        String url = exchange.getRequest().getPath().toString();
        rs.setUrl(url);
        rs.setMethod(method);
        rs.setMessage(e.getMessage());
        return Mono.just(ResponseEntity.badRequest().body(rs));
    }

}
