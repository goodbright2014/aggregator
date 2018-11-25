package com.skt.ehs.mbs.module.mct.http.handler;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.skt.ehs.mbs.db.maria.DBManager;
import com.skt.ehs.mbs.db.maria.vo.EHSBuildingInfo;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.router.RouteResult;
import io.netty.util.CharsetUtil;

/**
 * Area 정보를 요청했을 때 처리해주는 부분
 * @author YiSuHwan
 *
 */
public class GettingMapAreaHandler implements RouteServiceHandler {

	public static String CAMPUS = "campus";
	public static String CAMPUS_ID = "campus_id";
	public static String CAMPUS_NAME = "campus_name";
	public static String BUILDING_ID = "building_id";
	public static String BUILDING_NAME = "building_name";
	
	
	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {

		JSONObject retJson = null;
        List<EHSBuildingInfo> ret = null;
		try {
			ret = DBManager.getBuildingInfo();
			retJson = makeJsonReturnData(ret);
		} catch (Throwable e) {
			e.printStackTrace();
			retJson = makeJsonReturnData(null);
		}

		return CommonResponse.makeHttpBaseHeader(retJson);
	}
	
	
	public JSONObject makeJsonReturnData (List<EHSBuildingInfo> listdatas) {
		JSONObject obj = null;
		if (listdatas == null) {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_CAMPUS
        		, CommonResponse.FAIL_DB_ACCESS, CommonResponse.FAIL_DB_ACCESS_MSG);
		}
		else {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_CAMPUS
	        		, CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
			JSONArray areadList =  makeAreaList(listdatas);
			if (areadList != null)
				obj.put(CAMPUS, areadList);
		}
		
		return obj;
	}

	/**
	 * 
	 * @param listdatas JsonArray로 만들고자하는 데이터
	 * @return	null 데이터가 없는 상황, JSONArray 의 object Aread정보를 담은 Object
	 */
	public JSONArray makeAreaList(List<EHSBuildingInfo> listdatas) {
		if (listdatas.size() == 0)
			return null;
		
		JSONArray jArray = new JSONArray();
		for (EHSBuildingInfo info : listdatas) {
			JSONObject one = new JSONObject();

			one.put(CAMPUS_ID, info.getCampusID());
			one.put(CAMPUS_NAME, info.getCampusName());
			one.put(BUILDING_ID, info.getBuildID());
			one.put(BUILDING_NAME, info.getBuildName());
			
			jArray.add(one);
		}

		return jArray;
	}

}
