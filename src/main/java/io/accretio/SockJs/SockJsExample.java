package io.accretio.SockJs;

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


    EventBus eventBus;

    @Inject
    public SockJsExample(IdentityProviderManager identityProviderManager, NotificationService notificationService,
                         Vertx vertx, BridgeOptionImpl bridgeOption) {

        this.identityProviderManager = identityProviderManager;
        this.notificationService = notificationService;
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
        this.bridgeOption = bridgeOption;

    }

    public void init(@Observes Router router) {
        router.route("/ws/chat/*").handler(routingContext -> {
            roomId = routingContext.request().getParam("room_id");

            vertx.eventBus().consumer("chat.to.server", (Message<JsonObject> message) -> {
                LOG.info("Heyyyyyyyy");

                String type = message.body().getString("type");

                if (message.body() instanceof JsonObject) {
                    LOG.info("Mobile Recived");
                    switch (type) {
                        case "TEXT":
                        case "SONDAGE":
                        case "VOTE":
                        case "REACTION":
                            message.body().put("body", (message.body().getString("body")));
                            message.body().put("file", "");
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
                    setSocketValues(message, roomId,type);

                }

            });

            this.bridgeOption.setBridgeOptionMobile("chat.to.server" ,"chat.to.client/"+roomId );
            this.bridgeOption.setBridgeOptionWeb("chat.to.server" ,"chat.to.client/"+roomId );

            TcpEventBusBridge bridge = TcpEventBusBridge.create(vertx,
                    this.bridgeOption.getBridgeOptionMobile());

            bridge.listen(7000, res -> System.out.println("Ready"));
            routingContext.next();

        });
        router.route("/ws/chat/*").handler(eventBusHandler(this.eventBus));
        // router.route().handler(StaticHandler.create().setCachingEnabled(false));

    }

    private void setSocketValues(Message<JsonObject> message, String roomId, String type) {
        long timestamp = new Date().getTime() / 1000;
        message.body().put("timestamp", timestamp);
        message.body().put("type", type);
        LOG.info("Publishing from mobile with room id = "+roomId);
        vertx.eventBus().publish("chat.to.client/"+roomId, message.body());


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