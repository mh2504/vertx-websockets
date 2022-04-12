package com.demo.timebird.vertx_websockets;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WebSocketHandler implements Handler<ServerWebSocket> {

  private final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);
  public static final String PATH = "ws/simple/prices";
  public static final String DISCONNECT = "Disconnect me";

  @Override
  public void handle(ServerWebSocket ws) {
    if (PATH.equalsIgnoreCase(ws.path())) {
      LOG.info("Rejected wrong path: " + ws.path());
      ws.writeFinalTextFrame("Wrong path. Only " + PATH + " is accepted!");
      closeClient(ws);
      return;
    }
    LOG.info("Opening web socket connection: " + ws.path() + " ID: " + ws.textHandlerID());
    ws.accept();
    ws.frameHandler(frameHandler(ws));
    ws.endHandler(onClose -> LOG.info("Closed " + ws.textHandlerID()));
    ws.exceptionHandler(err -> LOG.error("FAILED! ", err));
    ws.writeTextMessage("Connected!");
  }

  private Handler<WebSocketFrame> frameHandler(ServerWebSocket ws) {
    return received -> {
      final String message = received.textData();
      LOG.debug("Received message " + message + " from client " + ws.textHandlerID());
      if (DISCONNECT.equalsIgnoreCase(message)) {
        LOG.info("Client close requested!");
        closeClient(ws);
      } else {
        ws.writeTextMessage("Not supported (" + message + ")");
      }
    };
  }

  private void closeClient(final ServerWebSocket ws) {
    ws.close((short) 1000, "Normal Closure");
  }
}
