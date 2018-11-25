package com.skt.ehs.mbs.module.mct.http.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.MBSServerMain;
import com.skt.ehs.mbs.db.maria.DBManager;
import com.skt.ehs.mbs.db.maria.vo.EHSFloorInfo;
import com.skt.ehs.mbs.module.mct.http.MCTHttpStaticServer;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

public class GettingMapFloorHandler implements RouteServiceHandler {
	final Logger logger = LoggerFactory.getLogger(GettingMapFloorHandler.class);
	
	public static String JSON_FLOOR_LIST = "floorList";
	public static String JSON_ID = "id";
	public static String JSON_NAME = "name";
	public static String JSON_IMG_URL = "image_url";
	public static String JSON_DIST_PIXEL = "dist_pixel";
	
	
	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {

		JSONObject retJson = null;
        List<EHSFloorInfo> ret = null;
		try {
			String building_id = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_BUILDING_ID);
			
			logger.info("Request Building ID : " + building_id);
			
			ret = DBManager.getFloorInfo(Integer.valueOf(building_id));
			logger.info("GettingMapFloorHandler gets result data from db : "+ret);
			retJson = makeJsonReturnData(ret);
		} catch (NumberFormatException e) {
			// 인자값이 잘 못 됨
			retJson = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_FLOOR
        		, CommonResponse.FAIL_INVALID_REQUEST, CommonResponse.FAIL_INVALID_REQUEST_MSG);
		}
	 	catch (Throwable e) {
			e.printStackTrace();
			retJson = makeJsonReturnData(null);
		}
		
		return CommonResponse.makeHttpBaseHeader(retJson);
	}

	
	public JSONObject makeJsonReturnData (List<EHSFloorInfo> listdatas) {
		JSONObject obj = null;
		if (listdatas == null) {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_FLOOR
        		, CommonResponse.FAIL_DB_ACCESS, CommonResponse.FAIL_DB_ACCESS_MSG);
		}
		else {
			// response 에 공통으로 들어가는 info (API_NAME, ERROR_CODE, ERROR_MESSAGE ..) 채운다
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_MAP_FLOOR
	        		, CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
			
			//JSONArray areaList =  makeAreaList(listdatas);
            List<JSONObject> areaList = makeAreaList(listdatas);

			if (areaList != null) {
                logger.info("GettingMapFloorHandler::areaList : "+areaList);
                obj.put(JSON_FLOOR_LIST, areaList);
                logger.info("GettingMapFloorHandler::makeJSONReturnData obj : "+obj);
            }
		}
		
		return obj;
	}

	/**
	 * 
	 * @param listdatas JsonArray로 만들고자하는 데이터
	 * @return	null 데이터가 없는 상황, JSONArray 의 object Aread정보를 담은 Object
	 */
	//public JSONArray makeAreaList(List<EHSFloorInfo> listdatas) {
    public List<JSONObject> makeAreaList(List<EHSFloorInfo> listdatas) {
		if (listdatas.size() == 0)
			return null;
		
		//JSONArray jArray = new JSONArray();
        List<JSONObject> floorList= new ArrayList<JSONObject>();

		for (EHSFloorInfo info : listdatas) {
			JSONObject one = new JSONObject();

			one.put(JSON_ID, info.getFloorID());
			one.put(JSON_NAME, info.getFloorName());
			one.put(JSON_IMG_URL, info.getImageName());
			one.put(JSON_DIST_PIXEL, info.getDist_pixel());
			
			//jArray.add(one);
            floorList.add(one);
		}

		//return jArray;
        return floorList;
	}
}
