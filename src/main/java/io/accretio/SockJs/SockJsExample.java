package io.accretio.SockJs;

import io.quarkus.security.identity.IdentityProviderManager;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.*;
import java.text.DateFormat;
import java.time.Instant;
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
    private String user ;


    EventBus eventBus;

    @Inject
    public SockJsExample(IdentityProviderManager identityProviderManager, NotificationService notificationService, Vertx vertx) {
        this.identityProviderManager = identityProviderManager;
        this.notificationService = notificationService;
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
    }


    public void init(@Observes Router router) {
        vertx.eventBus().consumer("chat.to.server", (Message<JsonObject> message) -> {
            String timestamp = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(Date.from(Instant.now()));

            vertx.eventBus().publish("chat.to.client", message.body() );
            LOG.info(message.body());
        });

        TcpEventBusBridge bridge = TcpEventBusBridge.create(
                vertx,
                new io.vertx.ext.bridge.BridgeOptions()
                        .addInboundPermitted(new PermittedOptions().setAddress("chat.to.server"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("chat.to.client"))
        );


        bridge.listen(7000, res -> System.out.println("Ready"));
        /*router.route("/ws/chat/*").handler(routingContext -> {
            user = routingContext.request().getParam("user");
            if(user == null || user.isEmpty()) {
                routingContext.response().setStatusCode(401).end();
                return;
            }
            identityProviderManager.authenticate(new TokenAuthenticationRequest(new AccessTokenCredential(user,routingContext)))
                    .thenAccept((securityIdentity) -> {
                        routingContext.setUser(new QuarkusHttpUser(securityIdentity));
                        routingContext.next();
                    })
                    .exceptionally(e -> {
                        Future.failedFuture(new HttpStatusException(401));
                        return null;
                    });

        });*/
        router.route("/ws/chat/*").handler(eventBusHandler(this.eventBus));
        router.route().handler(StaticHandler.create().setCachingEnabled(false));








    }

    private SockJSHandler eventBusHandler(EventBus eventBus ) {
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("chat.to.client"))
                .addInboundPermitted(new PermittedOptions().setAddressRegex("chat.to.server"));


        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);


        sockJSHandler.bridge(options, event -> {
            //LOG.info("d5al lel'event hedhi " + event.socket().localAddress() );
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                notificationService.onOpen(event,eventBus);
            }

            if (event.type() == BridgeEventType.SEND) {
                LOG.info("send");
                notificationService.onMessage(event,eventBus);
            }

            if (event.type() == BridgeEventType.SOCKET_CLOSED) {
                notificationService.onClose(event,eventBus);
            }

            event.complete(true);
        });
        return sockJSHandler;
    }

}