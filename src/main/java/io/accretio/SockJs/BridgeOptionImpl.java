package io.accretio.SockJs;

import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import org.jboss.logging.Logger;

import javax.inject.Singleton;

@Singleton
public class BridgeOptionImpl implements IBridgeOption {
    private static final Logger LOG = Logger.getLogger(BridgeOptionImpl.class);

    private io.vertx.ext.bridge.BridgeOptions bridgeOptionMobile ;
    private BridgeOptions bridgeOptionWeb ;


    public BridgeOptionImpl() {
        LOG.info("d5al lel constructeur");
        this.bridgeOptionMobile = new io.vertx.ext.bridge.BridgeOptions();
        this.bridgeOptionWeb = new BridgeOptions();
    }

    @Override
    public BridgeOptions getBridgeOptionWeb() {
        return bridgeOptionWeb;
    }

    @Override
    public io.vertx.ext.bridge.BridgeOptions getBridgeOptionMobile() {
        return bridgeOptionMobile;
    }

    @Override
    public void setBridgeOptionWeb(String channelIn, String channelOut) {
        if( !bridgeOptionWeb.getOutboundPermitteds().stream().filter(p -> p.getAddressRegex().equals(channelOut)).findFirst().isPresent())
            bridgeOptionWeb.addInboundPermitted(new PermittedOptions().setAddressRegex(channelIn))
                    .addOutboundPermitted(new PermittedOptions().setAddressRegex(channelOut));
    }

    @Override
    public void setBridgeOptionMobile(String channelIn, String channelOut) {
        if( !bridgeOptionMobile.getOutboundPermitteds().stream().filter(p -> p.getAddressRegex().equals(channelOut)).findFirst().isPresent())
            bridgeOptionMobile.addInboundPermitted(new PermittedOptions().setAddressRegex(channelIn))
                    .addOutboundPermitted(new PermittedOptions().setAddressRegex(channelOut));
    }
}