package com.skt.ehs.mbs.module.mct.http;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * HTTP 기능을 제공하기 위한 Server Main
 * @author YiSuHwan
 *
 */
public class MCTHttpStaticServer {
    public static final int PORT = 8082;
    
    
    public static final String ROUTE_PATH_CAMPUS_ID = "campus_id";
    public static final String ROUTE_PATH_BUILDING_ID = "building_id";
    public static final String ROUTE_PATH_FLOOR_ID = "floor_id";
    
    public static void startServer () throws Exception  {
    	
        // This is an example router, it will be used at HttpRouterServerHandler.
        //
        // For simplicity of this example, route targets are just simple strings.
        // But you can make them classes, and at HttpRouterServerHandler once you
        // get a target class, you can create an instance of it and dispatch the
        // request to the instance etc.

        /*

         Route Target :

         1. checkAuthority,
         2. RequestCampusInfo,
         3. RequestFloorInfo,
         4. RequestgetCollectedArea,
         5. RequestcollectedMagInfo,
         6. PutMageticData


          */
        @SuppressWarnings("unchecked")
		Router<String> router = new Router<String>()
               // 지역 정보와 캠퍼스 정보를 한번에 모아서  전달.
.GET("/getMapLocInfo/area", 									"GetLocationInfo")
               // 건물의 층정보를 모아서 전달 (층이름 + 지도 위치 전체 + 지도 정보 )
.GET("/getMapLocInfo/floor/:" + ROUTE_PATH_CAMPUS_ID + "/:" + ROUTE_PATH_BUILDING_ID
                        ,	"GetMapInfo")
.GET("/getCollectedArea/:" + ROUTE_PATH_CAMPUS_ID + "/:" + ROUTE_PATH_BUILDING_ID + "/:" + ROUTE_PATH_FLOOR_ID
                        , 	"CollectedArea")
.GET("/getMapLocInfo/magmap/:" + ROUTE_PATH_CAMPUS_ID + "/:" + ROUTE_PATH_BUILDING_ID
                                    + "/:" + ROUTE_PATH_FLOOR_ID	, 	"ShowFpMap")
// Collector 인증확인을 해준다.
.POST("/checkAuthority",										"authority")
               // 지자기데이터 저장 (TOOL -> 서버)
//.PUT("/addMagInfo/:" + ROUTE_PATH_CAMPUS_ID + "/:" + ROUTE_PATH_BUILDING_ID + "/:" + ROUTE_PATH_FLOOR_ID
   //                     ,	"StoreMagData")
  .PUT("/addMagInfo/"
                        ,	"StoreMagData")
//.GET("/mag/get/raw/:tagid", 				"RequestRawDataTag")
//RAW DATA를 삭제하는 Command 필요

.notFound("404 Not Found");

        //System.out.println(router);


        NioEventLoopGroup bossGroup   = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .childOption(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
             .childOption(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler())
             .childHandler(new MCTHttpRouterServerInitializer(router));

            Channel ch = b.bind(PORT).sync().channel();
            System.out.println("Server started: http://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();

            /*

             public void initChannel(SocketChannel ch) {

        ch.pipeline()
          .addLast(new HttpServerCodec())
          .addLast("logging", new LoggingHandler())
          .addLast("aggregator", new HttpObjectAggregator(1048576))
          .addLast(new MCTHttpRouterServerHandler(mctRouter))
          .addLast(badClientSilencer);
    }
             */
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
