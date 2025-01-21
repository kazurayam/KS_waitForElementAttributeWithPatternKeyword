package com.kazurayam.ks

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject


public class TestObjectFactory {
	
	static TestObject makeTestObject(String id, String xpath) {
		TestObject to = new TestObject(id)
		to.addProperty("xpath", ConditionType.EQUALS, xpath);
		return to;
	}
	
}
