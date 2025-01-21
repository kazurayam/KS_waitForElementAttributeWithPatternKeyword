import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC0 opens the target web page. It let you see how the page behaves.
 * 
 * Please see 3 seconds after the page load, the with of the target div element is changed
 * by JavaScript.
 * 
 */
String url = "https://kazurayam.github.io/KS_waitForElementAttributeWithPatternKeyword/page.html"
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(url);

WebUI.delay(3)

TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 3);
