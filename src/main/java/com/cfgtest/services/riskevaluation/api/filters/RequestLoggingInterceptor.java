package com.cfgtest.services.riskevaluation.api.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

// ServerHttpRequestDecorator -
// gives access to message body while Webflux is reading the stream
@Slf4j
public class RequestLoggingInterceptor extends ServerHttpRequestDecorator {
    public RequestLoggingInterceptor(ServerHttpRequest delegate) {
        super(delegate);
    }


    @Override
    public Flux<DataBuffer> getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return super.getBody().doOnNext(dataBuffer -> {
            try {
                Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                String body = baos.toString("UTF-8");
                // Logs information to console.
                log.info("Request: method={}, uri={}, payload={}, headers={}",
                        getDelegate().getMethod(), getDelegate().getURI(), body,
                        getDelegate().getHeaders().toString() );
            }catch (IOException e) {
                log.error("Exception when logging request body", e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("Exception when closing OutputStream: ", e.getMessage());
                    e.printStackTrace();
                }
            }

        });
    }
}
