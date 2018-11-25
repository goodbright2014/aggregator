package com.skt.ehs.mbs.module.mct.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.BadClientSilencer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;

public class MCTHttpRouterServerInitializer extends ChannelInitializer<SocketChannel> {
    //private final MCTHttpRouterServerHandler handler;
    private final BadClientSilencer       badClientSilencer = new BadClientSilencer();
    private final Router mctRouter;

    public MCTHttpRouterServerInitializer(Router router) {
        //handler = new MCTHttpRouterServerHandler(router);
        mctRouter = router;
    }

    @Override
    public void initChannel(SocketChannel ch) {
    	
        ch.pipeline()
          .addLast(new HttpServerCodec())
          .addLast("logging", new LoggingHandler())
          .addLast("aggregator", new HttpObjectAggregator(1048576))
          .addLast(new MCTHttpRouterServerHandler(mctRouter))
          .addLast(badClientSilencer);
    }
}