package com.skt.ehs.mbs.db.maria;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.conf.Configure;

/**
 * DB의 Connection(Session) 을 관리해주는 Class
 * @author YiSuHwan
 *
 */
public class MariaConnectionManager {
	final Logger logger = LoggerFactory.getLogger(MariaConnectionManager.class);
	
	private static MariaConnectionManager mInstance;
	
	private SqlSessionFactory mSqlSessionFactory;
	
	public static MariaConnectionManager getInstance() {
		if (mInstance == null)
			mInstance = new MariaConnectionManager();
		return mInstance;
	}
	
	/**
	 * 미리 설정값을 Load하여 Connection을 생성해주기 위한 함수
	 * @return
	 */
	public synchronized boolean makeConnectionPool()
	{
		String resource = Configure.MARIA_DB_CONFIGURE_LOCATION;
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		mSqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		return true;
	}
	
    public SqlSessionFactory getSqlSessionFactory() {
        return mSqlSessionFactory;
    }

}
