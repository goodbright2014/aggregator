package com.skt.ehs.mbs.module.mct.http.handler;

import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.db.maria.DBManager;
import com.skt.ehs.mbs.db.maria.vo.EHSUser;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

/**
 * 인증 확인을 위한 Handler
 * @author YiSuHwan
 *
 */
public class PostAuthorityHandler implements RouteServiceHandler {
	final Logger logger = LoggerFactory.getLogger(PostAuthorityHandler.class);
	
	public static String JSON_CMD_ID = "id";
	public static String JSON_CMD_PASSWORD = "password";
	
	
	
	public static final String R_KEY_ACCEPTED = "accept";
	/**
	 * 요청 결과값
	 */
	public static final String RETURN_AUTORITY_USER = "Y";
	public static final String RETURN_AUTORITY_WRONG_USER = "N";

	
	public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {
    	ByteBuf data = req.content();
    	EHSUser userData = parseBody(data.toString(StandardCharsets.UTF_8));

    	EHSUser existUserData = null;
		try {
			existUserData = DBManager.getUser(userData.getUSER_ID());
		} catch (Throwable e) {
			e.printStackTrace();
		}
    	if (existUserData == null)
    		return makeReturnData(false);
    	else {
    		if (userData.getPASSWORD().equals(existUserData.getPASSWORD()))
    			return makeReturnData(true);
    		else
    			return makeReturnData(false);
    	}
	}

	/**
	 * Client에서 전달된 Body 데이터를 Parsing한다.
	 * @param data
	 * @return
	 */
	public EHSUser parseBody(String data) {
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObject = null;
		
	    EHSUser retValue = new EHSUser();
	    
	    try {
    		jsonObject = (JSONObject)parser.parse(data);
	    } catch (ParseException e) {
	    	System.out.println("Parser Error : " + data);
	    	return null;
	    }
	    if (jsonObject == null) {
	    	return null;
	    }
	    
	    String tmp = (String) jsonObject.get(JSON_CMD_ID);
	    retValue.setUSER_ID(tmp);
	    tmp = (String) jsonObject.get(JSON_CMD_PASSWORD);
	    retValue.setPASSWORD(tmp);
	    
		return retValue;
	}

	/**
	 * 결과값을 생성한다.
	 * @param result	인증된 유저라면 true, 아니면 false
	 * @return	Client에 전달할 FullHttpResponse Object
	 */
	public FullHttpResponse makeReturnData (boolean result) {

		JSONObject obj = CommonResponse.makeCommonHeader(CommonResponse.API_CHECK_AUTHORITY
        		, CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
		if (result)
			obj.put(R_KEY_ACCEPTED, RETURN_AUTORITY_USER);
		else
			obj.put(R_KEY_ACCEPTED, RETURN_AUTORITY_WRONG_USER);
        
        FullHttpResponse res = CommonResponse.makeHttpBaseHeader(obj);
        
		return res;
	}

}
