package com.skt.ehs.mbs.module.mct.http.handler;

import java.util.List;

import org.json.simple.JSONObject;

import com.skt.ehs.mbs.module.mct.http.MCTHttpStaticServer;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

public class ShowFpMap implements RouteServiceHandler {

	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {
		JSONObject retJson = null;
		
		String floorID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_FLOOR_ID);
		String buildingID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_BUILDING_ID);
		String campusID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_CAMPUS_ID);
		
//		try {
//		} catch (NumberFormatException e) {
//			// 인자값이 잘 못 됨
//			retJson = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAG_MAGMAP
//        		, CommonResponse.FAIL_INVALID_REQUEST, CommonResponse.FAIL_INVALID_REQUEST_MSG);
//		}
//		catch (Throwable e) {
//			e.printStackTrace();
//			retJson = makeJsonReturnData(null);
//		}
		
		retJson = makeJsonReturnData(null);
		
		return CommonResponse.makeHttpBaseHeader(retJson);
	}

	private JSONObject makeJsonReturnData (List<String> listdatas) {
		JSONObject obj = null;
		
		if (listdatas == null) {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_MAGMAP
        		, CommonResponse.FAIL_DB_ACCESS, CommonResponse.FAIL_DB_ACCESS_MSG);
		}
//		else {
//			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_COLLECTED_AREA
//	        		, CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
//		}
		
		return obj;
	}
}
