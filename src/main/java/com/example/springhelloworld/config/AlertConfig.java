package com.example.springhelloworld.config;

import com.example.springhelloworld.domain.Alert;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "alerts")
@Data
public class AlertConfig {
    private List<Alert> alertList;

}
