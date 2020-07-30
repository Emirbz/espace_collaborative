package io.accretio.SockJs;

public interface IBridgeOption {

    public io.vertx.ext.web.handler.sockjs.BridgeOptions getBridgeOptionWeb();

    public io.vertx.ext.bridge.BridgeOptions getBridgeOptionMobile();

    public void setBridgeOptionWeb(String channelIn, String channelOut);

    public void setBridgeOptionMobile(String channelIn, String channelOut);
}