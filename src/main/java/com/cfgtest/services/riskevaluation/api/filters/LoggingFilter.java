package com.cfgtest.services.riskevaluation.api.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
public class LoggingFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        log.info("***** Intercepted by LoggingFilter *****");
       return webFilterChain.filter(buildServerWebExchangeDecorator().apply(serverWebExchange));
    }


    private Function<ServerWebExchange,ServerWebExchangeDecorator> buildServerWebExchangeDecorator() {
        long startTime = System.currentTimeMillis();
        return (exchange) -> {
            return new ServerWebExchangeDecorator(exchange) {
                @Override
                public ServerHttpRequest getRequest() {
//                    log.info("***** Creating instance of Request Logging Interceptor *****");
                    return new RequestLoggingInterceptor(super.getRequest());
                }

                @Override
                public ServerHttpResponse getResponse() {
//                    log.info("***** Creating instance of Response Logging Interceptor *****");
                    return new ResponseLoggingInterceptor(super.getResponse(),startTime);
                }
            };
        };
    }



}
