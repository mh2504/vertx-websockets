package com.demo.timebird.vertx_websockets;

import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PriceBroadcast {

  private static final Logger LOG = LoggerFactory.getLogger(PriceBroadcast.class);
  private final Map<String, ServerWebSocket> connectedClients = new HashMap<>();

  public PriceBroadcast(final Vertx vertx) {
    periodicUpdate(vertx);
  }

  private void periodicUpdate(final Vertx vertx) {
    vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), id -> {
      LOG.info("Push update to " + connectedClients.size() +" client(s)!");
      final String priceUpdate = new JsonObject()
        .put("symbol", "AMZN")
        .put("value", new Random().nextInt(1000))
        .toString();
      connectedClients.values().forEach(ws ->
        ws.writeTextMessage(priceUpdate)
      );
    });
  }

  public void register(final ServerWebSocket ws) {
    connectedClients.put(ws.textHandlerID(), ws);
  }

  public void unregister(final ServerWebSocket ws) {
    connectedClients.remove(ws.textHandlerID());
  }
}
