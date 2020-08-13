package io.accretio.SockJs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.accretio.Models.Message;
import io.accretio.Models.Reaction;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Services.*;
import io.accretio.Utils.FileUploader;
import io.minio.errors.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import org.jboss.logging.Logger;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Singleton
public class NotificationServiceImpl implements NotificationService {

    @Inject
    SondageService sondageService;

    @Inject
    MessageService messageService;

    @Inject
    UserService userService;

    @Inject
    ReactionService reactionService;
    @Inject
    ChoixService choixService;


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

        //     bridgeEvents.put("admin", event);
        //   JsonObject jsonObject = new JsonObject();
        //   jsonObject.put("address", "chat.to.client");


    }

    @Override
    public void onMessage(BridgeEvent event, EventBus eventBus) {

        JsonObject jsonObject = event.getRawMessage();
        JsonObject frontBody = new JsonObject(jsonObject.getString("body"));
        LOG.info("Front Body Web" + frontBody);
        String roomId = frontBody.getInteger("room_id").toString();
        String type = frontBody.getString("type");
        switch (type) {
            case "TEXT":
                publishText(jsonObject, frontBody, type, eventBus, roomId);
                break;

            case "REACTION":
                publishReaction(jsonObject, frontBody, type, eventBus, roomId);
                break;
            case "TYPING":
            case "VOTE":
                publishVote(jsonObject, frontBody, type, eventBus, roomId);
                break;
            case "SONDAGE":
                publishSondage(jsonObject, frontBody, type, eventBus, roomId);
                break;
            case "IMAGE":
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
                break;
        }
        if (!type.equals("SONDAGE") && !type.equals("TEXT") && !type.equals("REACTION") && !type.equals("VOTE")) {
            publish(jsonObject, type, eventBus, roomId);
        }


    }

    private void publishVote(JsonObject jsonObject, JsonObject frontBody, String type, EventBus eventBus, String roomId) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                User user = userService.findUserById(frontBody.getString("user_id"));
                choixService.addChoixEventBus(frontBody.getInteger("choix_id"), user);
                action.resolve(user);
            }).start();
        };

        Func function2 = (action, data) -> {
            jsonObject.put("user", objectMapper.writeValueAsString(data));
            jsonObject.put("message_id", frontBody.getInteger("message_id"));
            jsonObject.put("choix_id", frontBody.getInteger("choix_id"));
            publish(jsonObject, type, eventBus, roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation

    }

    private void publishReaction(JsonObject jsonObject, JsonObject frontBody, String type, EventBus eventBus, String roomId) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                User user = userService.findUserById(frontBody.getString("user_id"));
                Message message = new Message(frontBody.getInteger("message_id"));
                Reaction reaction = new Reaction(Reaction.reactionType.valueOf(frontBody.getString("body")) ,user);
                reactionService.addReactionEventBus(reaction,message);
                action.resolve(reaction);
            }).start();
        };

        Func function2 = (action, data) -> {
            jsonObject.put("body", objectMapper.writeValueAsString(data));
            jsonObject.put("message_id", frontBody.getInteger("message_id"));
            publish(jsonObject, type, eventBus, roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation


    }

    private void publishSondage(JsonObject jsonObject, JsonObject frontBody, String type, EventBus eventBus, String roomId) {
        try {
            Message sondage = objectMapper.readValue(frontBody.getString("body"), Message.class);
            Func function1 = (action, data) -> {
                new Thread(() -> {
                    Message submittedSondage = sondageService.addSondageEventBus(sondage);
                    action.resolve(submittedSondage);
                }).start();
            };

            Func function2 = (action, data) -> {
                jsonObject.put("body", objectMapper.writeValueAsString(data));
                publish(jsonObject, type, eventBus, roomId);
                action.resolve();
            };

            Promise.resolve()
                    .then(new Promise(function1))
                    .then(new Promise(function2))
                    .start();// start Promise operation


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void publishText(JsonObject jsonObject, JsonObject frontBody, String type, EventBus eventBus, String roomId) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                User user = userService.findUserById(frontBody.getString("user_id"));
                Message message = new Message();
                message.setBody(frontBody.getString("body"));
                message.setUser(user);
                message.setType(Message.type.TEXT);
                message.setRoom(new Room(frontBody.getInteger("room_id")));
                Message submittedMessage = messageService.addMessageEventBus(message);
                action.resolve(submittedMessage);
            }).start();
        };

        Func function2 = (action, data) -> {
            jsonObject.put("body", objectMapper.writeValueAsString(data));
            publish(jsonObject, type, eventBus, roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation


    }

    private void publish(JsonObject jsonObject, String type, EventBus eventBus, String roomId) {

        jsonObject.put("type", type);
        eventBus.publish("chat.to.client/" + roomId, jsonObject);
        LOG.info("Publishing from web to room with id : " + roomId);

    }

    @Override
    public void onClose(BridgeEvent event, EventBus eventBus) {
        bridgeEvents.remove("admin");
    }


}
