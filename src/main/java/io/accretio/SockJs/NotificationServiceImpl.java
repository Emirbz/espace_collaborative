package io.accretio.SockJs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Singleton
public class NotificationServiceImpl implements NotificationService {

    private static final String PREFERRED_USERNAME = "username";
    private Map<String, BridgeEvent> bridgeEvents = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(NotificationService.class);

    private final EventBus eventBus;
    private ObjectMapper objectMapper;

    @Inject
    public NotificationServiceImpl( EventBus eventBus) {
        this.eventBus = eventBus;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onOpen(BridgeEvent event, EventBus eventBus) {
        LOG.info("A socket was created for "+ event.socket().uri());
        bridgeEvents.put("admin", event);
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("address", "chat.to.client");
        

        event.socket().write(newMessage("Jhon Joined"));

        //event.socket().write(newMessage("Welcome "+ "kkkkkk"));
        //eventBus.publish("out", new JsonObject().put("body", "Notification " + "admin" + " joined").toString());
    }

    @Override
    public void onMessage(BridgeEvent event, EventBus eventBus) {
        LOG.info("A socket send "+ event.getRawMessage());
        JsonObject jsonObject = event.getRawMessage();
        jsonObject.put("body", jsonObject.getString("body"));
        eventBus.publish("chat.to.client", jsonObject);

    }

    @Override
    public void onClose(BridgeEvent event, EventBus eventBus) {
        bridgeEvents.remove("admin");
    }

    private String newMessage(String message) {
        return new JsonObject()
                .put("body", message)
                .put("address", "chat.to.client")
                .toString();
    }

}
