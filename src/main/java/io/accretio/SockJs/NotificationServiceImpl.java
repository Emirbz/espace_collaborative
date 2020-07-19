package io.accretio.SockJs;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jboss.logging.Logger;

import io.accretio.Models.Message;
import io.accretio.Services.MessageService;
import io.accretio.Utils.FileUploader;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.XmlParserException;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

@Singleton
public class NotificationServiceImpl implements NotificationService {

    @Inject
    MessageService messageService;

    private static final String PREFERRED_USERNAME = "username";
    private Map<String, BridgeEvent> bridgeEvents = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(NotificationService.class);

    private final EventBus eventBus;
    private ObjectMapper objectMapper;

    @Inject
    public NotificationServiceImpl(EventBus eventBus) {

        this.eventBus = eventBus;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onOpen(BridgeEvent event, EventBus eventBus) {

        bridgeEvents.put("admin", event);
        JsonObject jsonObject = new JsonObject();
        // JsonObject jsonObject = new JsonObject();
        jsonObject.put("address", "chat.to.client");

        // event.socket().write(newMessage("Jhon Joined"));

        // event.socket().write(newMessage("Welcome "+ "kkkkkk"));
        // eventBus.publish("out", new JsonObject().put("body", "Notification " +
        // "admin" + " joined").toString());
    }

    @Override
    public void onMessage(BridgeEvent event, EventBus eventBus) {
        long timestamp = new Date().getTime() / 1000;

        // LOG.info("A socket send "+ event.getRawMessage());

        JsonObject jsonObject = event.getRawMessage();

        JsonObject frontBody = jsonObject.getJsonObject("body");
        System.out.println(frontBody.getString("type"));
        if (frontBody.getString("type").equals("TEXT")) {
            jsonObject.put("body", (frontBody.getString("body")));
            jsonObject.put("type", "TEXT");
            jsonObject.put("file", "");

        } else if (frontBody.getString("type").equals("REACTION")) {
            jsonObject.put("body", (frontBody.getString("body")));
            jsonObject.put("type", "REACTION");
            jsonObject.put("file", "");

        } else if (frontBody.getString("type").equals("TYPING")) {
            jsonObject.put("body", (frontBody.getString("body")));
            jsonObject.put("type", "TYPING");
            jsonObject.put("file", "");
        } else if (frontBody.getString("type").equals("VOTE")) {
            jsonObject.put("body", (frontBody.getString("body")));
            jsonObject.put("type", "VOTE");
            jsonObject.put("file", "");
        } else if (frontBody.getString("type").equals("SONDAGE")) {
            jsonObject.put("body", (frontBody.getJsonObject("body")));
            jsonObject.put("type", "SONDAGE");
            jsonObject.put("file", "");
        }

        else if (frontBody.getString("type").equals("IMAGE")) {
            try {
                jsonObject.put("file", new FileUploader().addImage(frontBody.getString("file")));
            } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException
                    | NoSuchAlgorithmException | InsufficientDataException | InvalidExpiresRangeException
                    | InvalidResponseException | InternalException | XmlParserException | InvalidBucketNameException
                    | ErrorResponseException | RegionConflictException e) {
                e.printStackTrace();
            }
            jsonObject.put("type", "IMAGE");
            jsonObject.put("body", "");
        }

        jsonObject.put("firstName", frontBody.getString("firstName"));
        jsonObject.put("choix_id", frontBody.getInteger("choix_id"));
        jsonObject.put("lastName", frontBody.getString("lastName"));
        jsonObject.put("user_img", frontBody.getString("user_img"));
        jsonObject.put("room_id", frontBody.getInteger("room_id"));
        jsonObject.put("message_id", frontBody.getInteger("message_id"));
        jsonObject.put("user_id", frontBody.getString("user_id"));

        jsonObject.put("timestamp", timestamp);

        /*
         * Message message = new Message();
         * message.setBody(frontBody.getString("body")); Room room = new Room();
         * room.setId(frontBody.getInteger("room_id")); message.setRoom(room);
         * message.setType(Message.type.TEXT); User user = new User();
         * user.setId(frontBody.getString("user_id")); message.setUser(user);
         * 
         * 
         * 
         * messageService.addMessage(message);
         */
        eventBus.publish("chat.to.client", jsonObject);

        messageService.addMessage(new Message("test eventbus", "", Message.type.TEXT, null, null, null, null, null));

    }

    @Override
    public void onClose(BridgeEvent event, EventBus eventBus) {
        bridgeEvents.remove("admin");
    }

    private String newMessage(String message) {
        return new JsonObject().put("body", message).put("address", "chat.to.client").toString();
    }

}
