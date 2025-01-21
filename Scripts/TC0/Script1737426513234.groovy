import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * TC0
 *
 */

Path html = Paths.get(RunConfiguration.getProjectDir()).resolve("page.html")
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(html.toFile().toURI().toURL().toExternalForm());
TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 10);

boolean b0 = WebUI.waitForElementAttributeValue(tObj, 'style', "width: 0px;", 10, FailureHandling.STOP_ON_FAILURE)
if (b0) {
	String styleValue0 = WebUI.getAttribute(tObj, "style");
	WebUI.comment("styleValue0=" + styleValue0)
} else {
	KeywordUtil.markError("the style attribute with 'width: 0px' found not present")
}

WebUI.closeBrowser();

