package io.accretio.SockJs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.accretio.Models.Reaction;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Services.*;
import io.accretio.Utils.FileUploader;
import io.minio.errors.*;
import io.quarkus.security.identity.IdentityProviderManager;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.jboss.logging.Logger;
import org.riversun.promise.Func;
import org.riversun.promise.Promise;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SockJsExample {
    private static final Logger LOG = Logger.getLogger(SockJsExample.class);
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    private IdentityProviderManager identityProviderManager;
    private final NotificationService notificationService;
    private final Vertx vertx;
    private BridgeOptionImpl bridgeOption;
    private String roomId;
    private final ObjectMapper objectMapper;



    EventBus eventBus;

    @Inject
    UserService userService;

    @Inject
    MessageService messageService;


    @Inject
    ReactionService reactionService;

    @Inject
    SondageService sondageService;
    @Inject
    ChoixService choixService;

    @Inject
    public SockJsExample(IdentityProviderManager identityProviderManager, NotificationService notificationService,
                         Vertx vertx, BridgeOptionImpl bridgeOption) {

        this.identityProviderManager = identityProviderManager;
        this.notificationService = notificationService;
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.bridgeOption = bridgeOption;
        this.objectMapper = new ObjectMapper();

    }

    public void init(@Observes Router router) {
        router.route("/ws/chat/*").handler(routingContext -> {
            roomId = routingContext.request().getParam("room_id");

            vertx.eventBus().consumer("chat.to.server", (Message<JsonObject> message) -> {

                if (message.body() instanceof JsonObject) {
                    JsonObject frontBody = message.body();
                    String type = frontBody.getString("type");
                    LOG.info("Front Body mobile"+frontBody);

                    switch (type) {
                        case "TEXT":

                            publishText(frontBody,roomId,type);
                            break;
                            case "SONDAGE":
                                publishSondage(frontBody,roomId,type);
                                break;
                        case "VOTE":
                            publishVote(frontBody,roomId,type);
                            break;
                        case "REACTION":
                            publishReaction(frontBody,roomId,type);
                            break;
                        case "IMAGE":
                            try {
                                message.body().put("file", new FileUploader().addImage(message.body().getString("file")));
                            } catch (InvalidPortException | InvalidEndpointException | IOException | InvalidKeyException
                                    | NoSuchAlgorithmException | InsufficientDataException | InvalidExpiresRangeException
                                    | InvalidResponseException | InternalException | XmlParserException
                                    | InvalidBucketNameException | ErrorResponseException | RegionConflictException e) {
                                e.printStackTrace();
                            }
                            message.body().put("body", "");
                            break;
                    }
                   // pubish(message, roomId,type);

                }

            });
            this.bridgeOption.setBridgeOptionMobile("chat.to.server" ,"chat.to.client/"+roomId);
            this.bridgeOption.setBridgeOptionWeb("chat.to.server" ,"chat.to.client/"+roomId);

            TcpEventBusBridge bridge = TcpEventBusBridge.create(vertx,
                    this.bridgeOption.getBridgeOptionMobile());

            bridge.listen(7000, res -> System.out.println("Ready"));
            routingContext.next();

        });
        router.route("/ws/chat/*").handler(eventBusHandler(this.eventBus));
        // router.route().handler(StaticHandler.create().setCachingEnabled(false));

    }

    private void publishVote(JsonObject frontBody, String roomId, String type) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                User user = userService.findUserById(frontBody.getString("user_id"));
                choixService.addChoixEventBus(frontBody.getInteger("choix_id"), user);
                action.resolve(user);
            }).start();
        };

        Func function2 = (action, data) -> {
            frontBody.put("user", objectMapper.writeValueAsString(data));
            publish(frontBody, type, roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation

    }

    private void publishSondage(JsonObject frontBody, String roomId, String type) {
        Func function1 = (action, data) -> {
            io.accretio.Models.Message sondage = objectMapper.readValue(frontBody.getString("body"), io.accretio.Models.Message.class);
            LOG.info("next Step");
            new Thread(() -> {
                io.accretio.Models.Message submittedSondage = sondageService.addSondageEventBus(sondage);
                action.resolve(submittedSondage);
            }).start();
        };

        Func function2 = (action, data) -> {

            frontBody.put("body",objectMapper.writeValueAsString(data));

            publish(frontBody, type,  roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation



    }

    private void publishReaction(JsonObject frontBody, String roomId, String type) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                User user = userService.findUserById(frontBody.getString("user_id"));
                io.accretio.Models.Message message = new io.accretio.Models.Message(frontBody.getInteger("message_id"));
                Reaction reaction = new Reaction(Reaction.reactionType.valueOf(frontBody.getString("body")) ,user);
                reactionService.addReactionEventBus(reaction,message);
                action.resolve(reaction);
            }).start();
        };

        Func function2 = (action, data) -> {
            frontBody.put("body",objectMapper.writeValueAsString(data));
            publish(frontBody, type,  roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation


    }

    private void publishText(JsonObject frontBody, String roomId, String type) {
        Func function1 = (action, data) -> {
            new Thread(() -> {
                LOG.info("userid =  " + frontBody.getString("user_id"));
                User user = userService.findUserById(frontBody.getString("user_id"));
                io.accretio.Models.Message message = new io.accretio.Models.Message();
                message.setBody(frontBody.getString("body"));
                message.setUser(user);
                message.setType(io.accretio.Models.Message.type.TEXT);
                message.setRoom(new Room(frontBody.getInteger("room_id")));
                io.accretio.Models.Message submittedMessage = messageService.addMessageEventBus(message);
                action.resolve(submittedMessage);
            }).start();
        };

        Func function2 = (action, data) -> {
            frontBody.put("body",objectMapper.writeValueAsString(data));
            publish(frontBody, type,  roomId);
            action.resolve();
        };

        Promise.resolve()
                .then(new Promise(function1))
                .then(new Promise(function2))
                .start();// start Promise operation



    }

    private void publish(JsonObject message,  String type, String roomId) {
        //TODO timestamp
        message.put("type", type);
        LOG.info("Publishing from mobile with room id = "+roomId);
        vertx.eventBus().publish("chat.to.client/"+roomId, message);



    }

    private SockJSHandler eventBusHandler(EventBus eventBus) {

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);

        sockJSHandler.bridge(this.bridgeOption.getBridgeOptionWeb(), event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                LOG.info("1");

                notificationService.onOpen(event, eventBus);
            }

            if (event.type() == BridgeEventType.SEND) {
                LOG.info("2");
                notificationService.onMessage(event, eventBus);
            }

            if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                LOG.info("3");
                notificationService.onClose(event, eventBus);
            }

            event.complete(true);
        });
        return sockJSHandler;
    }





}