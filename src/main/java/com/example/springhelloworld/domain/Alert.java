package com.example.springhelloworld.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alerts")
public record Alert(
        Type alertType,
        double price) {

    public enum Type {
        cross_up,
        cross_down
    }
}
