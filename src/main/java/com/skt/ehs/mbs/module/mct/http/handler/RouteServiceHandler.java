package com.skt.ehs.mbs.module.mct.http.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

public interface RouteServiceHandler {

	/**
	 * Client에게서 전달받은 요청을 수행한다.
	 * @param data URL 이외의 데이터등을 넣어준다.
	 * @return json 형태의 결과 데이터
	 */
	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult);
}
