package com.example.springhelloworld.gotify;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

//@HttpExchange(url = "/")
public interface GotifyHttpClient {

    @PostExchange(value = "/message", contentType = MediaType.APPLICATION_JSON_VALUE)
    HttpHeaders sendMessage(@RequestHeader("X-Gotify-Key") String token, @RequestBody GotifyMessage message);
}
