package com.example.springhelloworld.gotify;

import com.example.springhelloworld.domain.Alert;
import com.example.springhelloworld.interfaces.CrossUpTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GotifyCrossUpTrigger implements CrossUpTrigger {
    private final GotifyService gotifyService;

    public GotifyCrossUpTrigger(GotifyService service) {
        gotifyService = service;
    }

    protected GotifyMessage getMessage(Alert alert, double newPrice) {
        var title = "BTCUSDT Cross UP";
        var message = "Price has crossed %s to %s".formatted(alert.price(), newPrice);
        return new GotifyMessage(title, message);
    }

    @Override
    public void alert(Alert alert, double newPrice) {
        gotifyService.alert(getMessage(alert, newPrice));
    }
}
