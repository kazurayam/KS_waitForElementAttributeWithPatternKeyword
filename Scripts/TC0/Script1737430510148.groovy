import static com.kazurayam.ks.TestObjectFactory.makeTestObject

import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * TC0 opens the target web page. It let you see how the page behaves.
 * 
 * Please see 3 seconds after the page load, the with of the target div element is changed
 * by JavaScript.
 * 
 */

Path html = Paths.get(RunConfiguration.getProjectDir()).resolve("page.html")
WebUI.openBrowser('')
WebUI.setViewPortSize(800, 600)
WebUI.navigateToUrl(html.toFile().toURI().toURL().toExternalForm());

WebUI.delay(3)

TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
WebUI.verifyElementPresent(tObj, 3);
