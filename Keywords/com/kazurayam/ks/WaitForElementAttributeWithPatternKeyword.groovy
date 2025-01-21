package com.kazurayam.ks

import java.text.MessageFormat
import java.time.Duration
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.FluentWait

import com.google.common.base.Function
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.constants.StringConstants
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.webui.keyword.internal.WebUIKeywordMain

public class WaitForElementAttributeWithPatternKeyword extends WebUIAbstractKeyword{

	@Override
	public Object execute(Object ...params) {
		TestObject to = getTestObject(params[0])
		String attributeName = (String) params[1]
		String pattern = (String) params[2]
		int timeOut = (int) params[3]
		FailureHandling flowControl = (FailureHandling)(params.length > 4 && params[4] instanceof FailureHandling ? params[4] : RunConfiguration.getDefaultFailureHandling())
		return waitForElementAttributeWithPattern(to,attributeName,pattern,timeOut,flowControl)
	}

	public boolean waitForElementAttributeWithPattern(TestObject to, String attributeName, String pattern, int timeOut, FailureHandling flowControl) {
		WebUIKeywordMain.runKeyword({
			boolean isSwitchIntoFrame = false
			try {
				WebUiCommonHelper.checkTestObjectParameter(to)
				logger.logDebug(StringConstants.COMM_LOG_INFO_CHECKING_ATTRIBUTE_NAME)
				if (attributeName == null) {
					throw new IllegalArgumentException(StringConstants.COMM_EXC_ATTRIBUTE_NAME_IS_NULL)
				}
				timeOut = WebUiCommonHelper.checkTimeout(timeOut)
				isSwitchIntoFrame = WebUiCommonHelper.switchToParentFrame(to, timeOut)
				WebElement foundElement = WebUIAbstractKeyword.findWebElement(to, timeOut)
				Pattern ptn = Pattern.compile(pattern)
				Boolean hasAttributeValue = new FluentWait<WebElement>(foundElement)
						.pollingEvery(Duration.ofMillis(500))
						.withTimeout(Duration.ofSeconds(timeOut))
						.until(new Function<WebElement, Boolean>() {
							@Override
							public Boolean apply(WebElement element) {
								// customized
								String attrValue = foundElement.getAttribute(attributeName)
								//println attrValue
								Matcher m = ptn.matcher(attrValue)
								return m.find()
							}
						})
				if (hasAttributeValue) {
					logger.logPassed(MessageFormat.format(StringConstants.KW_LOG_PASSED_OBJ_X_ATTRIBUTE_Y_VALUE_Z, to.getObjectId(), attributeName, pattern))
					return true
				}
			} catch (WebElementNotFoundException ex) {
				WebUIKeywordMain.stepFailed(MessageFormat.format(StringConstants.KW_LOG_WARNING_OBJ_X_IS_NOT_PRESENT, to.getObjectId()), flowControl, null, true)
			} catch (TimeoutException e) {
				logger.logWarning(MessageFormat.format(StringConstants.KW_LOG_FAILED_WAIT_FOR_OBJ_X_HAS_ATTRIBUTE_Y_VALUE_Z, to.getObjectId(), attributeName, attributeValue), null, e)
			} finally {
				if (isSwitchIntoFrame) {
					WebUiCommonHelper.switchToDefaultContent()
				}
			}
			return false
		}, flowControl, true, (to != null) ? MessageFormat.format(StringConstants.KW_MSG_CANNOT_WAIT_OBJ_X_ATTRIBUTE_Y_VALUE_Z, to.getObjectId(), attributeName, pattern)
		: StringConstants.KW_MSG_CANNOT_WAIT_OBJ_ATTRIBUTE_VALUE)
	}
}
