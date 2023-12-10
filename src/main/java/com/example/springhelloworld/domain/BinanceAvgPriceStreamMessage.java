package com.example.springhelloworld.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class BinanceAvgPriceStreamMessage {
    String e;
    Long E;
    String s;
    String i;
    Double w;
    Long T;

    @JsonCreator
    public BinanceAvgPriceStreamMessage(final String e, final Long E, final String s, final String i, final Double w, final Long T) {
        this.e = e;
        this.E = E;
        this.s = s;
        this.i = i;
        this.w = w;
        this.T = T;
    }

}
