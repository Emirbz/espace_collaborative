package io.accretio.SockJs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DateFormat;
import java.time.Instant;
import java.util.Date;
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
       // JsonObject jsonObject = new JsonObject();
        jsonObject.put("address", "chat.to.client");


       // event.socket().write(newMessage("Jhon Joined"));

        //event.socket().write(newMessage("Welcome "+ "kkkkkk"));
        //eventBus.publish("out", new JsonObject().put("body", "Notification " + "admin" + " joined").toString());
    }

    @Override
    public void onMessage(BridgeEvent event, EventBus eventBus) {
        String timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date.from(Instant.now()));

        LOG.info("A socket send "+ event.getRawMessage());
        JsonObject jsonObject = event.getRawMessage();

        jsonObject.put("body", jsonObject.getString("body"));
        jsonObject.put("user", "Amir Ben Zineb");
        jsonObject.put("user_img", "https://i.ibb.co/ncRPhQ9/66206883-10219342242417601-8507541653085487104-o.jpg?fbclid=IwAR1JncqNup_G0ciuTvrV7gHZ8iGlDihNntr-_ClfVz6nA43yLzWMIfjERJ8");
       // jsonObject.put("timestamp", timestamp);
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
