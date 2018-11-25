package com.skt.ehs.mbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.conf.Configure;
import com.skt.ehs.mbs.db.maria.MariaConnectionManager;
import com.skt.ehs.mbs.db.redis.RedisConnectionManager;
import com.skt.ehs.mbs.db.redis.RedisDBManager;
import com.skt.ehs.mbs.module.mct.http.MCTHttpStaticServer;
import com.skt.ehs.mbs.module.mct.lc.zmq.ZMQSubscriber;
import com.skt.ehs.mbs.module.mct.lc.zmq.ZMQSubscriber.OnRecvDataProcessor;
import com.skt.ehs.mbs.module.mct.lc.APDataProcessor;
/**
 * 서버를 구동하는 코드를 가지고 있는 부분
 * 관련 환경파일, DB Connection Manager들을 여기에서 미리 구동을 시켜준다.
 * @author YiSuHwan
 *
 */
public class MBSServerMain {
	final Logger logger = LoggerFactory.getLogger(MBSServerMain.class);
	
	public boolean startServer() {
		
		// 1. Load Configure
		try {
			Configure.loadConfigure();
		} catch (Exception e) {
			logger.error("Fail to load Configure. Can't start MBSServer.");
			return false;
		}
		
		// 2. Connect DataBases that are (Redis and Maria)
		if (RedisConnectionManager.getInstance().makeConnectionPool() == false) {
			logger.error("Fail to connect Redis. Can't start MBSServer.");
			return false;
		}
		if (MariaConnectionManager.getInstance().makeConnectionPool() == false) {
			logger.error("Fail to connect IntegrationDB. Can't start MBSServer.");
			return false;
		}
		// 3. Start LCInterface  .. needed error handling
		RedisDBManager redisDBManager = new RedisDBManager();
		OnRecvDataProcessor APdataProc = new APDataProcessor(redisDBManager);
		new ZMQSubscriber(APdataProc).start();

		// 4. Start HTTP Server
		try {
			logger.info("MCTHttpStaticServer starts");
			MCTHttpStaticServer.startServer();
		}catch (Exception e) {
			//TODO : 위에서 로딩했던 것들을 종료시켜주는 것이 필요하다.
			e.printStackTrace();
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		new MBSServerMain().startServer();
	}

}
