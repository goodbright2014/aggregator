package com.skt.ehs.mbs.module.mct.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.activation.MimetypesFileTypeMap;

import com.skt.ehs.mbs.module.mct.http.handler.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.router.RouteResult;
import io.netty.handler.codec.http.router.Router;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;


/**
 * 지자기 수집 툴 에서 요청한 내용을 실제로 처리하는 Class
 * 
 * @author YiSuHwan
 *
 */
public class MCTHttpRouterServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	final Logger logger = LoggerFactory.getLogger(MCTHttpRouterServerHandler.class);
	
	private final Router<String> router;

    public MCTHttpRouterServerHandler(Router<String> router) {
        this.router = router;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (HttpUtil.is100ContinueExpected(req)) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            logger.info(" is100ContinueExpected true uri = " + req.uri());
            return;
        }

        RouteResult<String> routeResult = router.route(req.method(), req.uri());
        
        if (routeResult.target().equals("GetLocationInfo")){
        	RouteServiceHandler handler = new GettingMapAreaHandler();
        	FullHttpResponse res = handler.processRequestMsg(req, routeResult);
            flushResponse(ctx,req, res);
        }
        else if (routeResult.target().equals("GetMapInfo")){
        	RouteServiceHandler handler = new GettingMapFloorHandler();
        	FullHttpResponse res = handler.processRequestMsg(req, routeResult);
            flushResponse(ctx,req, res);
        }
        else if (routeResult.target().equals("CollectedArea")){
        	RouteServiceHandler handler = new GetCollectedArea();
        	FullHttpResponse res = handler.processRequestMsg(req, routeResult);
            flushResponse(ctx,req, res);
        }
        else if (routeResult.target().equals("ShowFpMap")){
        	RouteServiceHandler handler = new ShowFpMap();
        	FullHttpResponse res = handler.processRequestMsg(req, routeResult);
            flushResponse(ctx,req, res);
        }
        else if (routeResult.target().equals("authority")) {
        	RouteServiceHandler handler = new PostAuthorityHandler();
        	FullHttpResponse res = handler.processRequestMsg(req, routeResult);
            flushResponse(ctx,req, res);
        }
		else if (routeResult.target().equals("StoreMagData")) { 	// added by jaehyu 2016-11-15. This is handling of "/collectedMagInfo/" API
			RouteServiceHandler handler = new StoreMagdataHandler();
			FullHttpResponse res = handler.processRequestMsg(req, routeResult);
			flushResponse(ctx,req, res);
		}

        else {
        	
            StringBuilder content = new StringBuilder();
            //content.append("router: \n" + router + "\n");
            content.append("req: " + req + "\n\n");
            content.append("routeResult: \n");
            content.append("target: " + routeResult.target() + "\n");
            content.append("pathParams: " + routeResult.pathParams() + "\n");
            content.append("queryParams: " + routeResult.queryParams() + "\n\n");
            content.append("allowedMethods: " + router.allowedMethods(req.uri()));
            
            sendString(ctx, req, content.toString());

        }
        
    }

    

    private static ChannelFuture flushResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {

        if (!HttpUtil.isKeepAlive(req)) {
            return ctx.writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
        } else {
            res.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            return ctx.writeAndFlush(res);
        }
    }
    
    /**
     * 에러 값 정송
     * @param ctx
     * @param status
     */
	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

		// Close the connection as soon as the error message is sent.
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}
    
	private static void sendFile(ChannelHandlerContext ctx, HttpRequest request)
	{
		File file = new File(System.getProperty("user.dir") + "/res/pages/index.html");
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException ignore) {
			ignore.printStackTrace();
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		
		long fileLength = -1;
		try {
			fileLength = raf.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		setContentTypeHeader(response, file);
		HttpUtil.setContentLength(response, fileLength);
		
		//setDateAndCacheHeaders(response, file);// 굳이 필요없을 것 같아 일단 빼둔다.
		
		if (HttpUtil.isKeepAlive(request)) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		// Write the initial line and the header.
		ctx.write(response);

		// Write the content.
		ChannelFuture sendFileFuture;
		ChannelFuture lastContentFuture;
		if (ctx.pipeline().get(SslHandler.class) == null) {
			sendFileFuture =
					ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength)
							, ctx.newProgressivePromise());
			// Write the end marker.
			lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		} else {
			try{
				sendFileFuture =
					ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
				ctx.newProgressivePromise());
			}catch (IOException e) {
				e.printStackTrace();
				sendFileFuture = null; // 임시로 처리해놓음 fidboys1
			}
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
			lastContentFuture = sendFileFuture;
		}
		 
		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
				if (total < 0) { // total unknown
					System.err.println(future.channel() + " Transfer progress: " + progress);
				} else {
					System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
				}
			}
			
			public void operationComplete(ChannelProgressiveFuture future) {
				System.err.println(future.channel() + " Transfer complete.");
			}
		});

        // Decide whether to close the connection or not.
		if (!HttpUtil.isKeepAlive(request)) {
			// Close the connection when the whole content is written out.
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	/**
	 * Sets the content type header for the HTTP Response
	 *
	 * @param response
	 *            HTTP response
	 * @param file
	 *            file to extract content type
	 */
	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	}
	
	
	private static void sendString (ChannelHandlerContext ctx, HttpRequest request, String sendString) {
        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(sendString, CharsetUtil.UTF_8)
        );
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        res.headers().set(HttpHeaderNames.CONTENT_LENGTH, sendString.length());
    	
        flushResponse(ctx, request, res);
	}

}
