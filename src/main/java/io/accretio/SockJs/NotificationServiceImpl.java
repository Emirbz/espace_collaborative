package io.accretio.SockJs;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.accretio.Services.SondageService;
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
import org.riversun.promise.Func;
import org.riversun.promise.Promise;


@Singleton
public class NotificationServiceImpl implements NotificationService {

    @Inject
    SondageService sondageService;

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

        String roomId  = frontBody.getInteger("room_id").toString();
        String type = frontBody.getString("type");
        switch (type) {
            case "TEXT":
            case "REACTION":
            case "TYPING":
            case "VOTE":
                jsonObject.put("body", (frontBody.getString("body")));
                jsonObject.put("file", "");
                break;
            case "SONDAGE":
                try {
                    Message sondage = objectMapper.readValue(frontBody.getString("body"),Message.class);
                    Func function1 = (action, data) -> {
                        new Thread(() -> {
                            Message submittedSondage = sondageService.addSondageEventBus(sondage);
                            action.resolve(submittedSondage);
                        }).start();
                    };

                    Func function2 = (action, data) -> {
                        jsonObject.put("body", objectMapper.writeValueAsString(data));
                        jsonObject.put("file", "");
                        setSocketValues(jsonObject, frontBody, type,eventBus,roomId);
                        action.resolve();
                    };

                    Promise.resolve()
                            .then(new Promise(function1))
                            .then(new Promise(function2))
                            .start();// start Promise operation



                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

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
        if (!type.equals("SONDAGE"))
        {
            setSocketValues(jsonObject, frontBody, type,eventBus,roomId);
        }





    }

    private void setSocketValues(JsonObject jsonObject, JsonObject frontBody, String type, EventBus eventBus, String roomId) {
        LOG.info("SetSocketValues");
        //TODO timestamp
        long timestamp = new Date().getTime() / 1000;
        jsonObject.put("type", type);
        jsonObject.put("firstName", frontBody.getString("firstName"));
        jsonObject.put("choix_id", frontBody.getInteger("choix_id"));
        jsonObject.put("lastName", frontBody.getString("lastName"));
        jsonObject.put("user_img", frontBody.getString("user_img"));
        jsonObject.put("room_id", frontBody.getInteger("room_id"));
        jsonObject.put("message_id", frontBody.getInteger("message_id"));
        jsonObject.put("user_id", frontBody.getString("user_id"));
        jsonObject.put("timestamp", timestamp);
        LOG.info("Publishing from web to room with id : "+roomId);
        eventBus.publish("chat.to.client/"+roomId, jsonObject);

    }

    @Override
    public void onClose(BridgeEvent event, EventBus eventBus) {
        bridgeEvents.remove("admin");
    }


}
