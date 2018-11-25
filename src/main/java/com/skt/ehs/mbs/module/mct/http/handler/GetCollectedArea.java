package com.skt.ehs.mbs.module.mct.http.handler;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.skt.ehs.mbs.db.maria.DBManager;
import com.skt.ehs.mbs.db.maria.vo.MBSCollectedCell;
import com.skt.ehs.mbs.module.mct.http.MCTHttpStaticServer;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

public class GetCollectedArea implements RouteServiceHandler {
	
	
	public static String JSON_CELL_SIZE = "cellSize";
	public static String JSON_PACKAGE_LIST = "packageList";
	public static String JSON_PACKAGE_ID = "package_id";
	public static String JSON_CELL_LIST = "cellList";
	public static String JSON_CELL_ID = "cell_id";

	
	
	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {
		
		JSONObject retJson = null;
		
		String floorID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_FLOOR_ID);
		String buildingID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_BUILDING_ID);
		String campusID = routeResult.param(MCTHttpStaticServer.ROUTE_PATH_CAMPUS_ID);
		
        List<MBSCollectedCell> ret = null;
		try {
			ret = DBManager.getCollectedMagCell(new Integer(floorID), new Integer(buildingID), new Integer(campusID));
			
			retJson = makeJsonReturnData(ret);
			
		} catch (NumberFormatException e) {
			// 인자값이 잘 못 됨
			retJson = CommonResponse.makeCommonHeader(CommonResponse.API_GET_COLLECTED_AREA
        		, CommonResponse.FAIL_INVALID_REQUEST, CommonResponse.FAIL_INVALID_REQUEST_MSG);
		}
		catch (Throwable e) {
			e.printStackTrace();
			retJson = makeJsonReturnData(null);
		}
		
		return CommonResponse.makeHttpBaseHeader(retJson);
	}


	private JSONObject makeJsonReturnData (List<MBSCollectedCell> listdatas) {
		JSONObject obj = null;
		
		if (listdatas == null) {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_COLLECTED_AREA
        		, CommonResponse.FAIL_DB_ACCESS, CommonResponse.FAIL_DB_ACCESS_MSG);
		}
		else {
			obj = CommonResponse.makeCommonHeader(CommonResponse.API_GET_COLLECTED_AREA
	        		, CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
			

//				obj.put(JSON_CELL_SIZE, );
			
			JSONArray cellLists =  makeCellList(listdatas);
			if (cellLists != null)
				obj.put(JSON_CELL_LIST, cellLists);
		}
		
		return obj;
	}
	
	/**
	 * Cell 정보의 배열을 Json형식으로 만든다.
	 * @param listdatas
	 * @return
	 */
	private JSONArray makeCellList(List<MBSCollectedCell> listdatas) {
		if (listdatas.size() == 0)
			return null;
	
		JSONArray jArray = new JSONArray();

		return jArray;
	}
}
