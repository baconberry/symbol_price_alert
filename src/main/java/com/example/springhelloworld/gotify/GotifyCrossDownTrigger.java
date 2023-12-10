package com.example.springhelloworld.gotify;

import com.example.springhelloworld.domain.Alert;
import com.example.springhelloworld.interfaces.CrossDownTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GotifyCrossDownTrigger implements CrossDownTrigger {
    private final GotifyService gotifyService;

    public GotifyCrossDownTrigger(GotifyService service) {
        gotifyService = service;
    }

    protected GotifyMessage getMessage(Alert alert, double newPrice) {
        var title = "BTCUSDT Cross DOWN";
        var message = "Price has crossed %s to %s".formatted(alert.price(), newPrice);
        return new GotifyMessage(title, message);
    }

    @Override
    public void alert(Alert alert, double newPrice) {
        gotifyService.alert(getMessage(alert, newPrice));
    }
}
