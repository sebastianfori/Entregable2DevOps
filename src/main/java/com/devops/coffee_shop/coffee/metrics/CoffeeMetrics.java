package com.devops.coffee_shop.coffee.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CoffeeMetrics {

    private final Counter ordersCreatedCounter;
    private final Counter ordersDeliveredCounter;

    public CoffeeMetrics(MeterRegistry registry) {
        this.ordersCreatedCounter = Counter.builder("coffee_orders_created_total")
                .description("Cantidad total de pedidos creados")
                .register(registry);

        this.ordersDeliveredCounter = Counter.builder("coffee_orders_delivered_total")
                .description("Cantidad total de pedidos entregados")
                .register(registry);
    }

    public void incrementOrdersCreated() {
        ordersCreatedCounter.increment();
    }

    public void incrementOrdersDelivered() {
        ordersDeliveredCounter.increment();
    }
}
