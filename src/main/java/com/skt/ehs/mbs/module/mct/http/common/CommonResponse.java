package com.skt.ehs.mbs.module.mct.http.common;

import org.json.simple.JSONObject;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class CommonResponse {
	
	/**
	 * 기본 공통으로 들어가는 내용
	 */
	private static final String API_NAME = "apiname";
	private static final String ERROR_CODE = "errorCode";
	private static final String ERROR_MESSAGE = "errorMsg";
	
	/**
	 * 요청 API 이름들
	 */
	public static final String API_CHECK_AUTHORITY = "checkAuthority"; 
	public static final String API_GET_MAP_CAMPUS = "getMapLocInfo/area";
	public static final String API_GET_MAP_FLOOR = "getMapLocInfo/floor";
	public static final String API_GET_MAP_MAGMAP = "getMapLocInfo/magmap";
	public static final String API_GET_COLLECTED_AREA = "getCollectedArea";
	public static final String API_ADD_MAG_INFO = "addMagInfo";


	/**
	 * 요청처리 결과 들
	 */
	public static final String SUCCESS = "0";
	public static final String SUCCESS_MSG = "success response";
	
	public static final String FAIL_NOT_EXIST = "-1001";
	public static final String FAIL_NOT_EXIST_MSG = "Request Data is not exist";
	
	public static final String FAIL_DB_ACCESS = "-1000";
	public static final String FAIL_DB_ACCESS_MSG = "FAIL TO DB ACCESS";

	
	public static final String FAIL_INVALID_REQUEST = "-1002";
	public static final String FAIL_INVALID_REQUEST_MSG = "Invalid Request Values";
	
	/**
	 *
	 * @param apiName
	 * @param ErrorCode
	 * @param Message
	 * @return
	 */	
	public static JSONObject makeCommonHeader(String apiName, String ErrorCode, String Message) {
        JSONObject obj = new JSONObject();
        obj.put(API_NAME, apiName);
        obj.put(ERROR_CODE, ErrorCode);
        obj.put(ERROR_MESSAGE, Message);
        
        return obj;
	}
	
	/**
	 * Json에 담겨진 내용을 이용하여 기본적인 FullHttpResponse Object를 생성해준다. 
	 * @param resJsonData  : return할 Json형식의 데이터
	 * @return FullHttpResponse Object
	 */
	public static FullHttpResponse makeHttpBaseHeader (JSONObject resJsonData) {
        FullHttpResponse res = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(resJsonData.toJSONString(), CharsetUtil.UTF_8)
        );
        res.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json ; charset=utf-8");
        res.headers().set(HttpHeaderNames.CONTENT_LENGTH, res.content().readableBytes());
        
		return res;
	}
}
