package com.skt.ehs.mbs.db.maria;

import java.util.List;

import com.skt.ehs.mbs.db.maria.dao.RawDataAPDAO;
import com.skt.ehs.mbs.db.maria.dao.RawDataTagDAO;
import com.skt.ehs.mbs.db.maria.vo.*;
import org.apache.ibatis.session.SqlSession;

import com.skt.ehs.mbs.conf.Configure;
import com.skt.ehs.mbs.db.maria.dao.EHSMapInfoDAO;
import com.skt.ehs.mbs.db.maria.dao.EHSUserDAO;
import com.skt.ehs.mbs.module.mct.http.common.AES256Manager;


/**
 * 통합 Maria DB에서 데이터를 Handler하는 함수들을 넣어둔다.
 * @author YiSuHwan
 *
 */
public class DBManager {

	
	
	/**
	 * 인증을 위한 사용자 정보를 획득한다.
	 */
	public static EHSUser getUser(String userID) throws Throwable {
		
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();

		SqlSession session = mngr.getSqlSessionFactory().openSession();
		
		try {
			EHSUserDAO mapper = session.getMapper(EHSUserDAO.class);
			AES256Manager keyMng = new AES256Manager(Configure.EHS_USER_KEY);
			String changedID = keyMng.aesEncode(userID);
			
			EHSUser user = mapper.selectUser(changedID);
			if (user != null) {
				user.setUSER_ID(userID);
				user.setPASSWORD(keyMng.aesDecode(user.getPASSWORD()));
			}
			
			return user;
		} catch (Exception e) {
			throw e;
		}
		finally {
		  session.close();
		}
	}
	
	/**
	 * 현재 미사용 코드 DB 확인을 위해서 추가된 Code이다.
	 * @return
	 */
	public static List<EHSUser> getUsers() throws Throwable{
		List<EHSUser> users = null;
		
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();
		
		try {
			EHSUserDAO mapper = session.getMapper(EHSUserDAO.class);

			users = (List<EHSUser>)mapper.selectUsers();
			if (users != null)
				System.out.println(" users.size() :: " + users.size());
			return users;
		} catch (Exception e) {
			throw e;
		}
		finally {
		  session.close();
		}
	}
	
	/**
	 * 건물의 층의 정보를 가져온다.
	 * @return
	 */
	public static List<EHSBuildingInfo> getBuildingInfo() throws Throwable{
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();
		List<EHSBuildingInfo> ret = null;
		try {
			EHSMapInfoDAO mapper = session.getMapper(EHSMapInfoDAO.class);
			ret = (List<EHSBuildingInfo>)mapper.selectArea();
			return ret;
		} catch (Exception e) {
			throw e;
		}
		finally {
		  session.close();
		}
	}
	
	/**
	 * 건물의 층의 정보를 가져온다.
	 * @return
	 */
	public static List<EHSFloorInfo> getFloorInfo(int buildingID) throws Throwable{
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();
		List<EHSFloorInfo> ret = null;
		try {
			EHSMapInfoDAO mapper = session.getMapper(EHSMapInfoDAO.class);
			ret = (List<EHSFloorInfo>)mapper.selectFloor(buildingID);
			return ret;
		} catch (Exception e) {
			throw e;
		}
		finally {
		  session.close();
		}
	}
	
	/**
	 * 확인 Cell의 정보를 얻어오는 함수
	 * @param floorID
	 * @param buildingID
	 * @param campusID
	 * @return
	 * @throws Throwable
	 */
	public static List<MBSCollectedCell> getCollectedMagCell(int floorID, int buildingID, int campusID)  throws Throwable {
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();
		List<MBSCollectedCell> ret = null;
		
		try {
			// TODO SUWHAN 작업을 진행하였으나 처리 방법이 변경되어 다시 짜야합니다.

			return ret;
		} catch (Exception e) {
			throw e;
		}
		finally {
		  session.close();
		}
	}



	public static void storeRawDataAP(RawDataAP rawDataAP) throws Throwable  {
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();

		try {
			RawDataAPDAO mapper = session.getMapper(RawDataAPDAO.class);
			mapper.insertRawDataAP(rawDataAP);
		} catch (Exception e) {
			throw e;
		}
		finally {
			session.close();
		}
	}
	/*
	*	storeMagInfoToRawDataTable(List<RawDataTag>)
	*
	 */
	public static void storeRawDataMag(RawDataTag rawDataTag) throws Throwable  {
		MariaConnectionManager mngr = MariaConnectionManager.getInstance();
		SqlSession session = mngr.getSqlSessionFactory().openSession();

		try {
			RawDataTagDAO mapper = session.getMapper(RawDataTagDAO.class);
			mapper.insertRawDataTag(rawDataTag);
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
