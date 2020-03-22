package io.accretio.SockJs;

import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public interface NotificationService {

    void onOpen(BridgeEvent event, EventBus eventBus);

    void onMessage(BridgeEvent event, EventBus eventBus);

    void onClose(BridgeEvent event, EventBus eventBus);

}