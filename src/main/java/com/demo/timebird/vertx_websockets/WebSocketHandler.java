package com.demo.timebird.vertx_websockets;

import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WebSocketHandler implements Handler<ServerWebSocket> {

  private final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);

  @Override
  public void handle(ServerWebSocket ws) {
    LOG.info("Opening web socket connection: " + ws.path() + " ID: " + ws.textHandlerID());
    ws.accept();
    ws.endHandler(onClose -> LOG.info("Closed " + ws.textHandlerID()));
    ws.exceptionHandler(err -> LOG.error("FAILED! ", err));
    ws.writeTextMessage("Connected!");
  }
}
