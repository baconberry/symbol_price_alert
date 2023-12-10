package com.example.springhelloworld.interfaces;

import com.example.springhelloworld.domain.Alert;

public interface CrossUpTrigger {

    void alert(Alert alert, double newPrice);
}
