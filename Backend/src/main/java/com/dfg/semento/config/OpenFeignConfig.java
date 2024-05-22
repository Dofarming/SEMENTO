package com.dfg.semento.config;
import feign.codec.Decoder;
import feign.Logger;
import feign.gson.GsonDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.dfg.semento")
public class OpenFeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder feignDecoder() {
        return new GsonDecoder();
    }
}
