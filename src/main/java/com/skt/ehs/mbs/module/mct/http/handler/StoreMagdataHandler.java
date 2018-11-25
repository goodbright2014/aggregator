package com.skt.ehs.mbs.module.mct.http.handler;

import com.skt.ehs.mbs.db.maria.DBManager;
import com.skt.ehs.mbs.db.maria.vo.RawDataAP;
import com.skt.ehs.mbs.db.maria.vo.RawDataTag;
import com.skt.ehs.mbs.module.mct.http.common.CommonResponse;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.router.RouteResult;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jaehyu on 2016-11-15.
 */
public class StoreMagdataHandler  implements RouteServiceHandler {

    final Logger logger = LoggerFactory.getLogger(StoreMagdataHandler.class);

    public FullHttpResponse processRequestMsg(FullHttpRequest req, RouteResult<String> routeResult) {
        ByteBuf data = req.content();
        List<RawDataTag> rawDataList =  parseBody(data.toString(StandardCharsets.UTF_8));
        for( RawDataTag rawDataTag : rawDataList ) {

            // Todo :
            // Searching Redis using composite key (with tag_id & sequence_num)
            // retreiveing hash correspond to the key
            // then build RawDataAP
            // then storeRawDataAP() & storeRawDataMag()

            RawDataAP rawDataAP = new RawDataAP();

            try {
                DBManager.storeRawDataAP(rawDataAP);
                DBManager.storeRawDataMag(rawDataTag);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return makeReturnData(true);
    }

/*
     * 결과값을 생성한다.
     * @param result	인증된 유저라면 true, 아니면 false
     * @return	Client에 전달할 FullHttpResponse Object
     */
    public FullHttpResponse makeReturnData (boolean result) {

        JSONObject obj = CommonResponse.makeCommonHeader(CommonResponse.API_ADD_MAG_INFO
                , CommonResponse.SUCCESS, CommonResponse.SUCCESS_MSG);
        //if (result)
        //    obj.put(R_KEY_ACCEPTED, RETURN_AUTORITY_USER);
        //else
        //    obj.put(R_KEY_ACCEPTED, RETURN_AUTORITY_WRONG_USER);

        FullHttpResponse res = CommonResponse.makeHttpBaseHeader(obj);

        return res;
    }
    /**
     * Client에서 전달된 Body 데이터를 Parsing한다.
     * @param data
     * @return
     */
    public List<RawDataTag> parseBody(String data) {

        List<RawDataTag> retValue = new ArrayList<RawDataTag>();

        JSONParser parser = new JSONParser();
        JSONObject request = null;
        try {
            request = (JSONObject)parser.parse(data);
        } catch (ParseException e) {
            System.out.println("Parser Error : " + data);
            return null;
        }
        if (request == null) {
            return null;
        }

        JSONArray rowList = (JSONArray) request.get("collection");
        Iterator i = rowList.iterator();
        while (i.hasNext()) {
            JSONObject rowData = (JSONObject)i.next();
            String date = (String)rowData.get("time");   // special handling date here
            Boolean bType = (Boolean)rowData.get("type");

            JSONArray cellList = (JSONArray)rowData.get("datas");
            Iterator j = cellList.iterator();
            while (j.hasNext()) {
                JSONObject cellData = (JSONObject)j.next();
                RawDataTag one = new RawDataTag();
                one.setRevision_collect(bType);
                one.setTag_id((String)cellData.get("tag_id"));
                one.setSequence_num((int) cellData.get("seq_num"));
                one.setSub_seq_num((int)cellData.get("seq_num_sub"));
                one.setPosition_x((int)cellData.get("pos_x"));
                one.setPosition_y((int)cellData.get("pos_y"));
                one.setMag_bh((double)cellData.get("mag_bh"));
                one.setMag_bv((double)cellData.get("mag_bv"));
                one.setNorth_dir((int)cellData.get("n_d"));
                one.setReverse_col((Boolean)cellData.get("n_b"));
                one.setC_cell_id((String)cellData.get("c_cell_id"));
                retValue.add(one);
            }

        }
        return retValue;
    }
}

/*   In case using org.json

import org.json.JSONArray;
import org.json.JSONObject;

        JSONObject request = new JSONObject(data);
        JSONObject collection = request.getJSONObject("collection");
        JSONArray rowList = collection.getJSONArray("collection");

        for (int i = 0; i < rowList.length(); i++) {
            JSONObject rowData = rowList.getJSONObject(i);

            String date = rowData.getString("time");
            boolean bType = rowData.getBoolean("type"); //  보정수집여부
            JSONObject cellListObj = rowData.getJSONObject("datas");
            JSONArray cellList =  cellListObj.getJSONArray("datas");

            for (int j = 0; j < cellList.length(); j++) {
                JSONObject cellData = cellList.getJSONObject(j);

                String tag_id = cellData.getString("tag_id");
                int seq_num = cellData.getInt("seq_num");

            }
        }
*/