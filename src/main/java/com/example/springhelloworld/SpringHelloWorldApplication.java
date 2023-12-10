package com.example.springhelloworld;

import com.example.springhelloworld.domain.BinanceAvgPriceStreamMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
@Slf4j
public class SpringHelloWorldApplication {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();
        long memoryMax = runtime.maxMemory();
        long memoryUsed = runtime.totalMemory() - runtime.freeMemory();

        log.info("CPU available: {}", runtime.availableProcessors());
        log.info("Max memory: {}MB", memoryMax / 1024 / 1024);
        log.info("Memory used: {}MB", memoryUsed / 1024 / 1024);
        var context = SpringApplication.run(SpringHelloWorldApplication.class, args);
        log.info("Memory used after context init: {}MB", memoryUsed / 1024 / 1024);

        var priceFetcher = context.getBean(SymbolPriceFetcher.class);
        priceFetcher.startPriceStream(true);
    }

    @Bean
    public Function<String, BinanceAvgPriceStreamMessage> textToBinanceAvgPrice(ObjectMapper mapper) {
        return text -> Try.of(() -> mapper.readValue(text, BinanceAvgPriceStreamMessage.class))
                .onFailure(t -> log.error("Could not parse {}, [{}]", BinanceAvgPriceStreamMessage.class.getName(), t.getMessage()))
                .getOrNull();
    }

}
