package com.skt.ehs.mbs.db.redis;


import com.skt.ehs.mbs.module.mct.lc.APDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

import com.skt.ehs.mbs.data.DataAP;
import com.skt.ehs.mbs.data.DataAcceleration;
import com.skt.ehs.mbs.data.DataMagnetic;
import com.skt.ehs.mbs.data.DataTag;

import com.skt.ehs.mbs.module.mct.lc.zmq.LCRecvDataParser;

/**
 * Redis를 접근을 쉽게하기 위한 Layer DB Interface API를 구현한다.
 * @author YiSuHwan
 *
 */
public class RedisDBManager {
	final Logger logger = LoggerFactory.getLogger(RedisDBManager.class);


	public void setAPInfo(byte[] data) {
		RedisConnectionManager redisConnMgr = RedisConnectionManager.getInstance();

		LCRecvDataParser lcRecvDataParser = new LCRecvDataParser();
		DataTag tagdata = null;
		try {
			tagdata = lcRecvDataParser.parseLCData(data);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		try (Jedis jedis = redisConnMgr.getConnection() ) {


			List<DataAP> apInfo = tagdata.getAps();

			// 여기에서의 현재시각은 단지 redis 에 저장되는 객체의 식별성을 위해 부여
			// 나중에 MCT에 의한 maria_db로의 RAW_DATA 적재시 MCT 적재요청시각에 맞는 현재시각으로 치환하여 RAW_DATA_TABLE에
			// INSERT 되게 된다. 이는 FP 빌드를 위한 식별을 위해 필요.

			Iterator iterator = apInfo.iterator();

			while (iterator.hasNext()) {

				// for each ap_info.
				DataAP dataAP = (DataAP)iterator.next();

				// build redis key
				UUID uid = UUID.randomUUID();
				String key = tagdata.getTagID() + tagdata.getSequenceNumber() + uid.toString();

				// set redis hash
				jedis.hset(key, "tag_id", tagdata.getTagID());
				jedis.hset(key, "sequence_no", String.valueOf(tagdata.getSequenceNumber()));
				jedis.hset(key, "ap_id", dataAP.getApID());
				jedis.hset(key, "rssi" , String.valueOf(dataAP.getRssi()));
			}
		};
	}
	public  int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	}

	// Todo : generic 사용하여 getAPInfo(key) 구현
	public void getAPInfo(String key) {
		RedisConnectionManager redisConnMgr = RedisConnectionManager.getInstance();


	}
}
