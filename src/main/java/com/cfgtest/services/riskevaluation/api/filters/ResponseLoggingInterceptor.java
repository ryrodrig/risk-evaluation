package com.cfgtest.services.riskevaluation.api.filters;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

// ServerHttpResponseDecorator -
// gives access to message body while Webflux is writing data to the stream
@Slf4j
public class ResponseLoggingInterceptor extends ServerHttpResponseDecorator {
    private long startTime;

    public ResponseLoggingInterceptor(ServerHttpResponse delegate, long startTime) {
        super(delegate);
        this.startTime = startTime;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<? extends DataBuffer> dataBuffer = Flux.from(body);
        return super.writeWith(dataBuffer.doOnNext(buffer -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                Channels.newChannel(baos).write(buffer.asByteBuffer().asReadOnlyBuffer());
                String respBody = baos.toString("UTF-8");
                log.info("Response({} ms): status={}, payload={}, headers={}",
                        (System.currentTimeMillis() - startTime) , getDelegate().getStatusCode()
                        , respBody, getDelegate().getHeaders().toString());
            } catch(IOException e) {
                log.error("Exception when logging response: {}" , e.getMessage());
                e.printStackTrace();
            }finally {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("Exception when closing OutputStream: ", e.getMessage());
                    e.printStackTrace();
                }
            }
        }));
    }
}
