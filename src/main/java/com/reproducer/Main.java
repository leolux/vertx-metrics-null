package com.reproducer;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.Match;
import io.vertx.ext.dropwizard.MatchType;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    // Enable Metrics
    VertxOptions options = new VertxOptions();
    DropwizardMetricsOptions metricsOptions = new DropwizardMetricsOptions();
    metricsOptions.setEnabled(true);
    metricsOptions.addMonitoredHttpServerUri(new Match().setValue("/*").setType(MatchType.REGEX));
    options.setMetricsOptions(metricsOptions);

    // Normal mode
     Vertx vertx = Vertx.vertx(options);
     deployVerticles(vertx);

    // Cluster mode
//    options.setClustered(true);
//    options.setHAEnabled(false);
//    options.setClusterManager(new HazelcastClusterManager());
//    Vertx.clusteredVertx(options, ar -> {
//      if (ar.succeeded()) {
//        Vertx vertx = ar.result();
//        deployVerticles(vertx);
//      }
//    });
  }

  private static void deployVerticles(Vertx vertx) {
    vertx.deployVerticle(PeriodicMetricsFetcher.class.getName(), res -> {
      if (res.succeeded()) {
        logger.info("************* PeriodicMetricsFetcher deployed successfully");
      } else {
        logger.info("************* PeriodicMetricsFetcher faild to deploy");
        vertx.close();
      }
    });
  }
}
