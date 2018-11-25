package com.skt.ehs.mbs.conf;

import java.io.IOException;

/**
 * MBS 이 구동 될때 필요한 설정파일을 미리 loading을 하고 사용할 수 있도록 제공
 * @author YiSuHwan
 *
 */
public class Configure {
	
	/**
	 * Config가 위치하는 곳을 저장
	 */
	private static String MCM_CONFIG_LOCATION = "/mcmconfigure"; 
	
	/**
	 * EHS User 암호화 Key값 : AES256Manager에서 사용
	 */
	public static String EHS_USER_KEY = "aes256-test-key!!";
	
	
	public static String MC_LC_ZEROMQ_IP = "localhost";
	public static String MC_LC_ZEROMQ_PORT = "0000";
	
	/**
	 * Redis 정보 - (Location Core와 연동해서 데이터를 저장함)
	 */
	public static String MBS_TEMP_INFO_REDIS_IP = "localhost";
	public static int MBS_TEMP_INFO_REDIS_PORT = 8001;
	public static String MBS_TEMP_INFO_REDIS_PASSWORD = "password";
	
	public static String MARIA_DB_CONFIGURE_LOCATION = "mybatis-config.xml";
	
	/**
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public static void loadConfigure() throws IOException, Exception {
		// TODO : 파일을 로딩해야 합니다.
		// TODO : 로딩한 값을 변수에 설정해 주어야 합니다.
	}
}
