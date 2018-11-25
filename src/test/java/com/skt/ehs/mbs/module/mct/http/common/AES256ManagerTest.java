package com.skt.ehs.mbs.module.mct.http.common;

import static org.junit.Assert.*;

import org.junit.Test;

import com.skt.ehs.mbs.conf.Configure;

public class AES256ManagerTest {

	@Test
	public void test01 () {
		try {
			AES256Manager data = new AES256Manager(Configure.EHS_USER_KEY);
			String decodedData = data.aesDecode("cbevIYxuXDMZYT4wnYKu+g==");
			System.out.println(" : " + decodedData);

			assertEquals("shyang", decodedData);
			
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}
