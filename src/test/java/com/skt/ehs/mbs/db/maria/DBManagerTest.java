package com.skt.ehs.mbs.db.maria;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.skt.ehs.mbs.conf.Configure;
import com.skt.ehs.mbs.db.maria.vo.EHSBuildingInfo;
import com.skt.ehs.mbs.db.maria.vo.EHSFloorInfo;
import com.skt.ehs.mbs.db.maria.vo.EHSUser;
import com.skt.ehs.mbs.db.maria.vo.MBSCollectedCell;



//MethodSorters.DEFAULT 	HashCode를 기반으로 순서가 결정되기 때문에 사용자가 예측하기 힘듭니다.
//MethodSorters.JVM 		JVM에서 리턴되는 순으로 실행됩니다. 때에 따라서 실행시 변경됩니다.
//MethodSorters.NAME_ASCENDING	메소드 명을 오름차순으로 정렬한 순서대로 실행됩니다.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBManagerTest {

	@BeforeClass
	public static void init() {
        System.out.println("init Run!!");
        
		// 1. Load Configure
		try {
			Configure.loadConfigure();
		} catch (Exception e) {
			System.out.println("Fail to load Configure. Can't start MBSServer.");
			return ;
		}
		
		if (MariaConnectionManager.getInstance().makeConnectionPool() == false) {
			System.out.println("Fail to connect IntegrationDB. Can't start MBSServer.");
			return ;
		}
	}
	
    @Test
    public void test01() {
        System.out.println("Test1 Run!!");
        EHSUser user = null;
		try {
			user = DBManager.getUser("shyang");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertTrue (user != null);
    }
    
    @Test
    public void test02() {
        System.out.println("Test2 Run!!");
        List<EHSUser> users = null;
		try {
			users = DBManager.getUsers();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        assertTrue (users != null);
    }
    
    @Test
    public void test03() {
        System.out.println("Test3 Run!!");
        List<EHSBuildingInfo> ret = null;
		try {
			ret = DBManager.getBuildingInfo();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ret != null && ret.size() > 0)
        	System.out.println("Test3 return !! " + ret.get(0).toString());
        assertTrue (ret != null);
    }
    
    @Test
    public void test04() {
        System.out.println("Test4 Run!!");
        List<EHSFloorInfo> ret = null;
		try {
			ret = DBManager.getFloorInfo(1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (ret != null && ret.size() > 0)
        	System.out.println("Test4 return !! " + ret.get(0).toString());
        assertTrue (ret.size() > 0);
        ret = null;
        try {
			ret = DBManager.getFloorInfo(123123123);
		} catch (Throwable e) {
			e.printStackTrace();
		}
        assertTrue(ret.size() == 0);
    }
    
    @Test
    public void test05() {
        List<MBSCollectedCell> ret = null;
		try {
			ret = DBManager.getCollectedMagCell(1, 1, 1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
        if (ret != null && ret.size() > 0)
        	System.out.println("Test5 return !! " + ret.get(0).toString());
        
        // TODO : 추가적인 작업 필요
        assertTrue (ret.size() > 0);
    }
}
