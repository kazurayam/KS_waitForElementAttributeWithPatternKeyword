import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.WaitForElementAttributeWithPatternKeyword as KW
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

TestObject makeTO(String id, String xpath) {
	TestObject to = new TestObject(id)
	to.addProperty("xpath", ConditionType.EQUALS, xpath);
	return to;	
}

Path html = Paths.get(RunConfiguration.getProjectDir()).resolve("page.html")
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(html.toFile().toURI().toURL().toExternalForm());
TestObject tObj = makeTO("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 10);

boolean b1 = new KW().waitForElementAttributeWithPattern(tObj, 'style', "width\\s*:\\s*[0-9]+px;?", 10, FailureHandling.STOP_ON_FAILURE)
if (b1) {
	String styleValue1 = WebUI.getAttribute(tObj, "style");
	WebUI.comment("styleValue1=" + styleValue1)
}

WebUI.closeBrowser();

