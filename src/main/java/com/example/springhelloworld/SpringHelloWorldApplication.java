package com.example.springhelloworld;

import com.example.springhelloworld.domain.BinanceAvgPrigeStreamMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
@Slf4j
public class SpringHelloWorldApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(SpringHelloWorldApplication.class, args);

        var priceFetcher = context.getBean(SymbolPriceFetcher.class);
        priceFetcher.startPriceStream();
    }

    @Bean
    public Converter<String, BinanceAvgPrigeStreamMessage> textToBinanceAvgPrice(ObjectMapper mapper) {
        return text -> Try.of(() -> mapper.readValue(text, BinanceAvgPrigeStreamMessage.class))
                .onFailure(t -> log.error("Could not parse {}, [{}]", BinanceAvgPrigeStreamMessage.class.getName(), t.getMessage()))
                .getOrNull();
    }

}
