# waitForElementAttributeWithPattern --- a custom keyword in Katalon Studio

## Application UnderTest

Please click the following link to open a public URL as a testbed:

-   [The target web page](https://kazurayam.github.io/KS_waitForElementAttributeWithPatternKeyword/page.html)

This web page starts with an initial view like this:

![AUT initial](./images/AUT_initial.png)

Please note a grey band in the background of "Foo" text spans full width of the browser window.

In a couple seconds after the page loading, a poor animation begins: the grey band starts shrinking:

![AUT middle](./images/AUT_middle.png)

finally, the width of the grey band reaches almost 0px.

![AUT settled](./images/AUT_settled.png)

You can read the source of the web page

    <!DOCTYPE html>
    <html>
    <head>
      <style>
        * {
          margin:0;
          padding: 0;
        }
        .bi_sidenav {
          background-color: #ccc;
          color: #c36;
        }
      </style>
      <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    </head>
    <body>

      <div id="bi_mySidenav" class="bi_sidenav xh-hightlight"><span>Foo</span></div>

      <script type="text/javascript" defer>
        const fn = function() {
          const finalWidth = Math.floor(10 * Math.random())
          $("#bi_mySidenav").animate({width: finalWidth + 'px'}, 'slow')
        }
        setTimeout(fn, 1000);
      </script>
    </body>
    </html>

If you look into the DOM of this web page, you would notice that the `<div id="bi_mySidenav">` element initially has no `style` attribute. But as soon as JavaScript fires, the `style` attribute is created. The `style` attribute contains the `width: Xpx` value where the digit `X` quickly moves down near to 0.

![AUT DOM](./images/AUT_DOM.png)

Now I want to test if the `<div id="bi_mySidenav">` element has an `style` attribute with value of `"width: Xpx;"` (X is a certain small digit). How can I do it?

## Problem : TC0 fails using WebUI.getAttribute

At firt, let me show you a naive test case: `TC0`.

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

This test case immediately fails:

    2025-01-21 21:48:08.374 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2025-01-21 21:48:08.379 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/TC0
    2025-01-21 21:48:09.616 INFO  c.k.k.core.webui.driver.DriverFactory    - Starting 'Chrome' driver
    1月 21, 2025 9:48:09 午後 org.openqa.selenium.remote.DesiredCapabilities chrome
    情報: Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`
    2025-01-21 21:48:09.661 INFO  c.k.k.c.w.util.WebDriverPropertyUtil     - User set preference: ['excludeSwitches', '[enable-automation]']
    2025-01-21 21:48:09.703 INFO  c.k.k.core.webui.driver.DriverFactory    - Action delay is set to 0 milliseconds
    Starting ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7}) on port 21461
    Only local connections are allowed.
    Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
    ChromeDriver was started successfully on port 21461.
    1月 21, 2025 9:48:13 午後 org.openqa.selenium.remote.ProtocolHandshake createSession
    情報: Detected dialect: W3C
    2025-01-21 21:48:13.735 INFO  c.k.k.core.webui.driver.DriverFactory    - sessionId = e165446d40c951bf3cc6ee7651c38366
    2025-01-21 21:48:13.934 INFO  c.k.k.core.webui.driver.DriverFactory    - browser = Chrome 131.0.0.0
    2025-01-21 21:48:13.956 INFO  c.k.k.core.webui.driver.DriverFactory    - platform = Mac OS X
    2025-01-21 21:48:13.978 INFO  c.k.k.core.webui.driver.DriverFactory    - seleniumVersion = 3.141.59
    2025-01-21 21:48:13.999 INFO  c.k.k.core.webui.driver.DriverFactory    - proxyInformation = ProxyInformation { proxyOption=NO_PROXY, proxyServerType=HTTP, username=, password=********, proxyServerAddress=, proxyServerPort=0, executionList="", isApplyToDesiredCapabilities=true }
    attrValue=
    2025-01-21 21:48:15.736 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ Test Cases/TC0 FAILED.
    Reason:
    Assertion failed:

    assert attrValue.length() > 0
           |         |        |
           ''        0        false

        at TC0.run(TC0:23)
        at com.kms.katalon.core.main.ScriptEngine.run(ScriptEngine.java:194)
        at com.kms.katalon.core.main.ScriptEngine.runScriptAsRawText(ScriptEngine.java:119)
        at com.kms.katalon.core.main.TestCaseExecutor.runScript(TestCaseExecutor.java:448)
        at com.kms.katalon.core.main.TestCaseExecutor.doExecute(TestCaseExecutor.java:439)
        at com.kms.katalon.core.main.TestCaseExecutor.processExecutionPhase(TestCaseExecutor.java:418)
        at com.kms.katalon.core.main.TestCaseExecutor.accessMainPhase(TestCaseExecutor.java:410)
        at com.kms.katalon.core.main.TestCaseExecutor.execute(TestCaseExecutor.java:285)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:144)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:135)
        at TempTestCase1737463681594.run(TempTestCase1737463681594.groovy:25)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

    2025-01-21 21:48:15.748 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ Test Cases/TC0 FAILED.
    Reason:
    Assertion failed:

    assert attrValue.length() > 0
           |         |        |
           ''        0        false

        at TC0.run(TC0:23)
        at com.kms.katalon.core.main.ScriptEngine.run(ScriptEngine.java:194)
        at com.kms.katalon.core.main.ScriptEngine.runScriptAsRawText(ScriptEngine.java:119)
        at com.kms.katalon.core.main.TestCaseExecutor.runScript(TestCaseExecutor.java:448)
        at com.kms.katalon.core.main.TestCaseExecutor.doExecute(TestCaseExecutor.java:439)
        at com.kms.katalon.core.main.TestCaseExecutor.processExecutionPhase(TestCaseExecutor.java:418)
        at com.kms.katalon.core.main.TestCaseExecutor.accessMainPhase(TestCaseExecutor.java:410)
        at com.kms.katalon.core.main.TestCaseExecutor.execute(TestCaseExecutor.java:285)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:144)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:135)
        at TempTestCase1737463681594.run(TempTestCase1737463681594.groovy:25)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

    2025-01-21 21:48:16.353 WARN  c.k.katalon.core.main.TestCaseExecutor   - Failed connecting to tab web socket.
    2025-01-21 21:48:16.365 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/TC0

The call to `WebUI.getAttribute(TestObject, 'style')` failed because the `<div id="bi_mySidenav" >` element has no `style` attribute on the page load. The `style` attribute will be added 1 second after the page load.

## Problem : TC1 fails using WebUI.waitForElementAttributeValue

Next approach: let me try `WebUI.waitForElementAttributeValue` keyword to wait for the `style` attribute to appear on the DOM.

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

The `TC1` failed as well.

    2025-01-21 22:33:27.619 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2025-01-21 22:33:27.623 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/TC1
    2025-01-21 22:33:28.613 INFO  c.k.k.core.webui.driver.DriverFactory    - Starting 'Chrome' driver
    1月 21, 2025 10:33:28 午後 org.openqa.selenium.remote.DesiredCapabilities chrome
    情報: Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`
    2025-01-21 22:33:28.645 INFO  c.k.k.c.w.util.WebDriverPropertyUtil     - User set preference: ['excludeSwitches', '[enable-automation]']
    2025-01-21 22:33:28.678 INFO  c.k.k.core.webui.driver.DriverFactory    - Action delay is set to 0 milliseconds
    Starting ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7}) on port 18534
    Only local connections are allowed.
    Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
    ChromeDriver was started successfully on port 18534.
    1月 21, 2025 10:33:32 午後 org.openqa.selenium.remote.ProtocolHandshake createSession
    情報: Detected dialect: W3C
    2025-01-21 22:33:32.255 INFO  c.k.k.core.webui.driver.DriverFactory    - sessionId = 2fdc793e6fdb5c8fa496c3616a3c7c36
    2025-01-21 22:33:32.287 INFO  c.k.k.core.webui.driver.DriverFactory    - browser = Chrome 131.0.0.0
    2025-01-21 22:33:32.299 INFO  c.k.k.core.webui.driver.DriverFactory    - platform = Mac OS X
    2025-01-21 22:33:32.336 INFO  c.k.k.core.webui.driver.DriverFactory    - seleniumVersion = 3.141.59
    2025-01-21 22:33:32.373 INFO  c.k.k.core.webui.driver.DriverFactory    - proxyInformation = ProxyInformation { proxyOption=NO_PROXY, proxyServerType=HTTP, username=, password=********, proxyServerAddress=, proxyServerPort=0, executionList="", isApplyToDesiredCapabilities=true }
    2025-01-21 22:33:37.679 WARN  .k.b.WaitForElementAttributeValueKeyword - Object 'Side Navigation Window' does not have attribute 'style' with value 'width: 0px;'
    2025-01-21 22:33:38.029 INFO  com.kms.katalon.core.util.KeywordUtil    - the style attribute value: width: 1px;
    2025-01-21 22:33:38.283 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ Test Cases/TC1 FAILED.
    Reason:
    com.kms.katalon.core.exception.StepErrorException: the style attribute with 'width: 0px' found not present.
        at com.kms.katalon.core.util.KeywordUtil.markError(KeywordUtil.java:67)
        at TC1.run(TC1:30)
        at com.kms.katalon.core.main.ScriptEngine.run(ScriptEngine.java:194)
        at com.kms.katalon.core.main.ScriptEngine.runScriptAsRawText(ScriptEngine.java:119)
        at com.kms.katalon.core.main.TestCaseExecutor.runScript(TestCaseExecutor.java:448)
        at com.kms.katalon.core.main.TestCaseExecutor.doExecute(TestCaseExecutor.java:439)
        at com.kms.katalon.core.main.TestCaseExecutor.processExecutionPhase(TestCaseExecutor.java:418)
        at com.kms.katalon.core.main.TestCaseExecutor.accessMainPhase(TestCaseExecutor.java:410)
        at com.kms.katalon.core.main.TestCaseExecutor.execute(TestCaseExecutor.java:285)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:144)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:135)
        at TempTestCase1737466401774.run(TempTestCase1737466401774.groovy:25)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

    2025-01-21 22:33:38.292 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ Test Cases/TC1 FAILED.
    Reason:
    com.kms.katalon.core.exception.StepErrorException: the style attribute with 'width: 0px' found not present.
        at com.kms.katalon.core.util.KeywordUtil.markError(KeywordUtil.java:67)
        at TC1.run(TC1:30)
        at com.kms.katalon.core.main.ScriptEngine.run(ScriptEngine.java:194)
        at com.kms.katalon.core.main.ScriptEngine.runScriptAsRawText(ScriptEngine.java:119)
        at com.kms.katalon.core.main.TestCaseExecutor.runScript(TestCaseExecutor.java:448)
        at com.kms.katalon.core.main.TestCaseExecutor.doExecute(TestCaseExecutor.java:439)
        at com.kms.katalon.core.main.TestCaseExecutor.processExecutionPhase(TestCaseExecutor.java:418)
        at com.kms.katalon.core.main.TestCaseExecutor.accessMainPhase(TestCaseExecutor.java:410)
        at com.kms.katalon.core.main.TestCaseExecutor.execute(TestCaseExecutor.java:285)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:144)
        at com.kms.katalon.core.main.TestCaseMain.runTestCase(TestCaseMain.java:135)
        at TempTestCase1737466401774.run(TempTestCase1737466401774.groovy:25)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

    2025-01-21 22:33:38.562 WARN  c.k.katalon.core.main.TestCaseExecutor   - Failed sending HTTP request.
    2025-01-21 22:33:38.571 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/TC1

Why the `TC1` failed?

The `TC1` has a line of statement:

    boolean b1 = WebUI.waitForElementAttributeValue(tObj, 'style', "width: 0px;", 3, FailureHandling.STOP_ON_FAILURE)

Here I wrote the expected value of the `style` attribute to be `width: 0px;`. But actually the value was NOT equal to `width: 0px;`. Possiblly something like `width: 6px;` (not 0px, which changes at random).

The problem is that the `WebUI.waitForElementAttributeValue` keyword requires a concrete string value as the 3rd parameter. Therefore you have no way to give the keyword a right value to test equality.

## Solution: TC2 using a custom keyword: waitForElementAttributeWithPattern

I have developed a custom keyword class: `com.kazurayam.ks.WaitForElementAttributeWithPattern`, which is a bit of modification of the builtin `waitForElementAttributeValue` keyword. The custom keyword takes a Regular expression as the 3rd parameter. It waits for the specified attribute with value that matches the Regular expression.

A sample usage of the custom keyword: `TC2`

    import static com.kazurayam.ks.TestObjectFactory.makeTestObject

    import com.kazurayam.ks.WaitForElementAttributeWithPatternKeyword as KW
    import com.kms.katalon.core.model.FailureHandling
    import com.kms.katalon.core.testobject.TestObject
    import com.kms.katalon.core.util.KeywordUtil
    import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

    /**
     * TC2 demonstrates the waitForElementAttributeWithPattern keyword
     * 
     * TC2 passes.
     */
    String url = "https://kazurayam.github.io/KS_waitForElementAttributeWithPatternKeyword/page.html"
    WebUI.openBrowser('')
    WebUI.setViewPortSize(800, 600)
    WebUI.navigateToUrl(url);

    TestObject tObj = makeTestObject("Side Navigation Window", "//*[@id='bi_mySidenav']");
    WebUI.verifyElementPresent(tObj, 10);

    String pattern = "width\\s*:\\s*[0-9]+px;?"

    boolean b2 = new KW().waitForElementAttributeWithPattern(tObj, 'style', pattern, 3, FailureHandling.STOP_ON_FAILURE)
    if (b2) {
        String styleValue2 = WebUI.getAttribute(tObj, "style");
        WebUI.comment("styleValue2=" + styleValue2)
    } else {
        KeywordUtil.markError("the style attribute that match with the pattern '${pattern}' not present")
    }

    WebUI.closeBrowser();

Please note that the `TC2` specifies a Regular expression:

    String pattern = "width\\s*:\\s*[0-9]+px;?"

This pattern can match any values of the following

-   `width: 0px;`

-   `width:6px;`

-   `height:100px; width:8px;`

When I ran TC2, it passed.

    2025-01-21 22:58:22.842 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
    2025-01-21 22:58:22.847 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/TC2
    2025-01-21 22:58:24.048 INFO  c.k.k.core.webui.driver.DriverFactory    - Starting 'Chrome' driver
    1月 21, 2025 10:58:24 午後 org.openqa.selenium.remote.DesiredCapabilities chrome
    情報: Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`
    2025-01-21 22:58:24.077 INFO  c.k.k.c.w.util.WebDriverPropertyUtil     - User set preference: ['excludeSwitches', '[enable-automation]']
    2025-01-21 22:58:24.112 INFO  c.k.k.core.webui.driver.DriverFactory    - Action delay is set to 0 milliseconds
    Starting ChromeDriver 131.0.6778.204 (52183f9e99a61056f9b78535f53d256f1516f2a0-refs/branch-heads/6778_155@{#7}) on port 24063
    Only local connections are allowed.
    Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
    ChromeDriver was started successfully on port 24063.
    1月 21, 2025 10:58:28 午後 org.openqa.selenium.remote.ProtocolHandshake createSession
    情報: Detected dialect: W3C
    2025-01-21 22:58:28.834 INFO  c.k.k.core.webui.driver.DriverFactory    - sessionId = 955d118c4221c5b1b0cd003ef42b8ebe
    2025-01-21 22:58:28.882 INFO  c.k.k.core.webui.driver.DriverFactory    - browser = Chrome 131.0.0.0
    2025-01-21 22:58:28.897 INFO  c.k.k.core.webui.driver.DriverFactory    - platform = Mac OS X
    2025-01-21 22:58:28.918 INFO  c.k.k.core.webui.driver.DriverFactory    - seleniumVersion = 3.141.59
    2025-01-21 22:58:28.941 INFO  c.k.k.core.webui.driver.DriverFactory    - proxyInformation = ProxyInformation { proxyOption=NO_PROXY, proxyServerType=HTTP, username=, password=********, proxyServerAddress=, proxyServerPort=0, executionList="", isApplyToDesiredCapabilities=true }
    2025-01-21 22:58:32.950 INFO  c.k.k.c.keyword.builtin.CommentKeyword   - styleValue2=width: 1px;
    2025-01-21 22:58:33.291 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/TC2

## Description

How is the custom keyword is implemented?

Please read the source code at

-   <https://github.com/kazurayam/KS_waitForElementAttributeWithPatternKeyword/blob/main/Keywords/com/kazurayam/ks/WaitForElementAttributeWithPatternKeyword.groovy>

This source code is a mimic of the built-in `WebUI.waitForElementAttributeValue` keyword. I copied and pasted the original source into my custom keyword class, and modified just a few lines so that it matches text using regular expression. You can find the source of the built in keyword in `<Katalon Studio installation folder>/Contents/Eclipse/configuration/resources/source/com.kms.katalon.core.webui/com.kms.katalon.core.webui-sources.jar`
