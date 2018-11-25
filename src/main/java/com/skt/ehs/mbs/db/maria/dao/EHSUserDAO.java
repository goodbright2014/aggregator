package com.skt.ehs.mbs.db.maria.dao;

import java.util.List;

import com.skt.ehs.mbs.db.maria.vo.EHSUser;

/**
 * 사용자 정보 관련된 처리를 모아둔다.
 * @author YiSuHwan
 *
 */
public interface EHSUserDAO {
	
	//@Select("SELECT (USER_ID, USER_GROUP_ID, PASSWORD) FROM p_user WHERE USER_ID = #{userID}")
	public EHSUser selectUser(String userID);
	//@Select("SELECT (USER_ID, USER_GROUP_ID, PASSWORD) FROM p_user")
	public List<EHSUser> selectUsers();
}
