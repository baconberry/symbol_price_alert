package com.example.springhelloworld;

import com.example.springhelloworld.domain.BinanceAvgPriceStreamMessage;
import com.example.springhelloworld.interfaces.PriceChangeListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
public record SymbolPriceFetcher(
        Function<String, BinanceAvgPriceStreamMessage> textToBinanceAvgPrice,
        List<PriceChangeListener> priceChangeListeners
) {

    @SneakyThrows
    public void startPriceStream(boolean isRunForever) {
        WebSocketClient client = new StandardWebSocketClient();
        var session = client.execute(handler(), "wss://stream.binance.com:9443/ws/btcusdt@avgPrice")
                .get();

        log.info("WSS session open? [{}]", session.isOpen());
        log.info("Price fetcher stream started");
        while (isRunForever) {
            Thread.sleep(1000);
        }
    }

    public WebSocketHandler handler() {
        return new AbstractWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                log.debug("Got message [{}]", message.getPayload());
                var avgPrice = textToBinanceAvgPrice.apply(message.getPayload());
                log.debug("Latest avg price ${}", avgPrice.getW());
                priceChangeListeners.forEach(listener -> listener.acceptPrice(avgPrice.getW()));
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                log.error("Stream connection closed, exitting");
                System.exit(11);
            }
        };
    }

}
