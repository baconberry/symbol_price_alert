package com.example.springhelloworld;

import com.example.springhelloworld.config.AlertConfig;
import com.example.springhelloworld.domain.Alert;
import com.example.springhelloworld.interfaces.CrossDownTrigger;
import com.example.springhelloworld.interfaces.CrossUpTrigger;
import com.example.springhelloworld.interfaces.PriceChangeListener;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@Slf4j
public final class AlertTrigger implements PriceChangeListener {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private double price = 0.0;
    private final List<Alert> alerts;
    private final List<CrossUpTrigger> crossUpTriggerList;
    private final List<CrossDownTrigger> crossDownTriggersList;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public AlertTrigger(AlertConfig config,
                        List<CrossUpTrigger> crossUpTriggers,
                        List<CrossDownTrigger> crossDownTriggers,
                        AsyncTaskExecutor taskExecutor) {
        alerts = Objects.requireNonNull(config.getAlertList());
        assert !alerts.isEmpty();
        this.crossUpTriggerList = crossUpTriggers;
        this.asyncTaskExecutor = taskExecutor;
        this.crossDownTriggersList = crossDownTriggers;
    }

    @Override
    public void acceptPrice(double price) {
        Try.run(lock.readLock()::lock)
                .andThen(() -> triggerNewPrice(price))
                .andFinally(lock.readLock()::unlock);
        Try.run(lock.writeLock()::lock)
                .andThen(() -> this.price = price)
                .andFinally(lock.writeLock()::unlock);
    }

    public void triggerNewPrice(double newPrice) {
        if (price == 0.0) {
            return;
        }
        alerts.forEach(a -> matchAlert(a, newPrice));
        log.debug("Prev price {}, new price {}", this.price, newPrice);
    }

    public void matchAlert(Alert alert, double newPrice) {
        switch (alert.alertType()) {
            case cross_up:
                if (price < alert.price() && newPrice >= alert.price()) triggerAlert(alert, newPrice);
                break;
            case cross_down:
                if (price > alert.price() && newPrice <= alert.price()) triggerAlertDown(alert, newPrice);
                break;
            default:
                return;
        }
    }

    public void triggerAlert(Alert alert, double newPrice) {
        crossUpTriggerList.forEach(trigger ->
            asyncTaskExecutor.execute(() -> trigger.alert(alert, newPrice))
        );
    }

    public void triggerAlertDown(Alert alert, double newPrice) {
        crossDownTriggersList.forEach(trigger ->
            asyncTaskExecutor.execute(() -> trigger.alert(alert, newPrice))
        );
    }

}
