package com.example.springhelloworld.interfaces;

@FunctionalInterface
public interface PriceChangeListener {

    void acceptPrice(double price);
}
