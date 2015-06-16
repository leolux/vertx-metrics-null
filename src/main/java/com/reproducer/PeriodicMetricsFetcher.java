package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;

/**
 *
 */
public class PeriodicMetricsFetcher extends AbstractVerticle {

  public void start() {
    MetricsService metricsService = MetricsService.create(vertx);

    vertx.setPeriodic(1500, l -> {
      JsonObject snapshot = metricsService.getMetricsSnapshot(vertx);
      System.out.println("Metrics: "+snapshot);
    });
  }

}
