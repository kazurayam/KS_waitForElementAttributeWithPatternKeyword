import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.ks.WaitForElementAttributeWithPatternKeyword as KW
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC2 demonstrates the waitForElementAttributeWithPattern keyword
 * 
 * TC2 passes.
 */

Path html = Paths.get(RunConfiguration.getProjectDir()).resolve("page.html")
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(html.toFile().toURI().toURL().toExternalForm());
TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 10);

String pattern = "width\\s*:\\s*[0-9]+%;?"

boolean b2 = new KW().waitForElementAttributeWithPattern(tObj, 'style', pattern, 10, FailureHandling.STOP_ON_FAILURE)
if (b2) {
	String styleValue2 = WebUI.getAttribute(tObj, "style");
	WebUI.comment("styleValue2=" + styleValue2)
} else {
	KeywordUtil.markError("the style attribute that match with the pattern '${pattern}' not present")
}

WebUI.closeBrowser();

