package browserStackTests;

import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.percy.selenium.Percy;

public class PercyTest {

	WebDriver driver;
	Percy percy;
	JavascriptExecutor jse;
	WebDriverWait wait;

	@BeforeTest
	public void setup() {
		Hashtable<String, String> capsHashtable = new Hashtable<String, String>();
		capsHashtable.put("browser", "chrome");
		capsHashtable.put("browser_version", "latest");
		capsHashtable.put("os", "Windows");
		capsHashtable.put("os_version", "10");
		capsHashtable.put("build", "percy-test-1");
		capsHashtable.put("name", "Percy Test");
		capsHashtable.put("browserstack.selenium_version","3.141.59");

		String key;
		DesiredCapabilities caps = new DesiredCapabilities();

		Set<String> keys = capsHashtable.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext()) {
			key = itr.next();
			caps.setCapability(key, capsHashtable.get(key));
		}

		
		try {
			String URL = "https://" + System.getenv("BROWSERSTACK_USERNAME") + ":" + System.getenv("BROWSERSTACK_ACCESS_KEY") + "@hub-cloud.browserstack.com/wd/hub";
			driver = new RemoteWebDriver(new java.net.URL(URL), caps);
			percy = new Percy(driver);
			jse = (JavascriptExecutor) driver;
			wait = new WebDriverWait(driver, 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sampleTest() {
		// Locators
		By acceptCookie = By.id("onetrust-accept-btn-handler");
		By addToBasket = By.xpath("//button[@class='button button--transactional js-add-to-basket-button button--nav add-to-basket__form-button']");
		By checkout = By.xpath("//a[@class='button button--nav button--transactional basket-nav__button basket-nav__default-cta js-basket-nav-button']");
		By firstName = By.xpath("//input[@name='firstName']");
		By lastName = By.xpath("//input[@name='lastName']");
		
		// Test Steps
		try {
			// Navigate to Product Page
			driver.get("https://www.dyson.co.uk/vacuum-cleaners/sticks/dyson-outsize-stick/dyson-outsize-absolute");

			// Accept Cookie
			try {
				wait.until(ExpectedConditions.elementToBeClickable(acceptCookie));
				driver.findElement(acceptCookie).click();
			} catch(Exception e) {
				System.out.println("Accept Cookie Not Found!");
			}
			wait.until(ExpectedConditions.elementToBeClickable(addToBasket));

			percy.snapshot("Product Page");

			// Click on Add To Basket
			driver.findElement(addToBasket).click();
			wait.until(ExpectedConditions.elementToBeClickable(checkout));

			percy.snapshot("Basket Page");
			
			// Click on Checkout
			driver.findElement(checkout).click();
			
			// Enter First Name
			wait.until(ExpectedConditions.elementToBeClickable(firstName));
			driver.findElement(firstName).sendKeys("TestName");

			// Enter Last Name
			wait.until(ExpectedConditions.elementToBeClickable(lastName));
			driver.findElement(lastName).sendKeys("LName");

//			percy.snapshot("Checkout Page");
			percy.snapshot("Checkout Page");

			WebElement email = driver.findElement(By.cssSelector("input[id^='email']"));
			email.sendKeys("test_test@test.com");

			WebElement mobile = driver.findElement(By.cssSelector("input[id^='mobileNumber']"));
			mobile.sendKeys("0000000");

			WebElement deliveryButton = driver.findElement(By.cssSelector("button[class='button button--transactional button--large checkout__button js-checkout-button']"));
			deliveryButton.click();

			wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[id^='postcodeLookup']")));
			percy.snapshot("Delivery Page");

			jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Execution Completed!\"}}");
		} catch(Exception e) {
			jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"Execution Not Completed!\"}}");
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
