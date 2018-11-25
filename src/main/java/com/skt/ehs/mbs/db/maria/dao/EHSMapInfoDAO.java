package com.skt.ehs.mbs.db.maria.dao;

import java.util.List;

import com.skt.ehs.mbs.db.maria.vo.EHSBuildingInfo;
import com.skt.ehs.mbs.db.maria.vo.EHSFloorInfo;

/**
 * 지도 정보를 획득하기 위한 일련의 API들을 모아 둔다.
 * @author YiSuHwan
 *
 */
public interface EHSMapInfoDAO {

	public List<EHSBuildingInfo> selectArea();
	
	public List<EHSFloorInfo> selectFloor(int buildingID);
}
