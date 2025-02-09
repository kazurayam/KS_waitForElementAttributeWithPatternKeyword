import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * TC1 uses the WebUI.waitForElementAttributeValue keyword.
 * 
 * TC1 fails, however, because the value of the 'style' attributes changes case by base dynamically
 * while the built-in keyword requires you to specify the expected value as a constant.
 * 
 */

String url = "https://kazurayam.github.io/KS_waitForElementAttributeWithPatternKeyword/page.html"
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(url);

TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 10);

boolean b1 = WebUI.waitForElementAttributeValue(tObj, 'style', "width: 0px;", 3, FailureHandling.STOP_ON_FAILURE)
if (b1) {
	String styleValue1 = WebUI.getAttribute(tObj, "style");
	WebUI.comment("styleValue1=" + styleValue1)
} else {
	KeywordUtil.markError("the style attribute with 'width: 0px' found not present. ")
	KeywordUtil.logInfo("the style attribute value: ${WebUI.getAttribute(tObj, 'style')}")
}

WebUI.closeBrowser();

