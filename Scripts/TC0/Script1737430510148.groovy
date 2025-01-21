import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC0 calls WebUI.getAttribute() keyword as soon as the page is loaded. 
 * This test fails.
 * 
 */
String url = "https://kazurayam.github.io/KS_waitForElementAttributeWithPatternKeyword/page.html"
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(url);

TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 3);

String attrValue = WebUI.getAttribute(tObj, 'style')
println "attrValue=" + attrValue

assert attrValue != null
assert attrValue.length() > 0

WebUI.closeBrowser()