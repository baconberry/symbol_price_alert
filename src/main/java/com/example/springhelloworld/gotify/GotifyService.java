package com.example.springhelloworld.gotify;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Component
public class GotifyService {
    private final String gotifyToken;
    private final String gotifyServer;
    private final RestTemplate restTemplate;
    private final GotifyHttpClient gotifyHttpClient;

    public GotifyService(
            @Value("${alerts.gotify.token}") String token,
            @Value("${alerts.gotify.url}") String server
    ) {
        gotifyToken = token;
        gotifyServer = server;
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(gotifyServer));
        gotifyHttpClient = HttpServiceProxyFactory.builderFor(RestTemplateAdapter.create(restTemplate))
                .build()
                .createClient(GotifyHttpClient.class);
    }


    public void alert(GotifyMessage gotifyMessage) {
        log.info("Sending gotify alert [{}]", gotifyMessage);

        Try.run(() -> {
                    var headers = gotifyHttpClient.sendMessage(
                            gotifyToken,
                            gotifyMessage
                    );
                    log.debug("Got gotify response headers for message [{}]", headers);
                }
        ).onFailure(t -> log.error("Got error sending gotify message, {}", t.getMessage(), t));
    }
}
