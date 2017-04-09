import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.Assert;

import java.net.HttpURLConnection;
import java.net.URL;

public class TestNG {
	private WebDriver driver;
	private String baseURL = "http://blog.dev/";
//	private String baseURL = "http://derekgillett.com/";

	private int getResponseCode(String address) {
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL(address);
			connection = (HttpURLConnection) url.openConnection();
			return connection.getResponseCode();
		} catch (Exception e1) {
			e1.printStackTrace();
			return 0;
		}
	}
    
	// check archive page OK
	@Test
	public void archivePageOK() {
		Assert.assertEquals(this.getResponseCode(baseURL+"archive"), HttpURLConnection.HTTP_OK);
	}
	
	// check contact page OK
	@Test
	public void contactPageOK() {
		Assert.assertEquals(this.getResponseCode(baseURL+"content/contact"), HttpURLConnection.HTTP_OK);
	}
	
	// ensure no error messages appear (just check on homepage)
	@Test
	public void noErrorMessages() {
		driver.get(baseURL);

		By byXpath = By.cssSelector("h4.element-invisible");
		List<WebElement> elements = driver.findElements(byXpath);
		Assert.assertTrue(elements.isEmpty());
	}

	// do a search and check the search results page appears
	@Test
	public void searchWorks() {
		driver.get(baseURL);

		By byXpath;
		WebElement element;
		
		byXpath = By.cssSelector("input#edit-search-block-form--2");
		element = driver.findElement(byXpath);
		element.sendKeys("test");
		
		byXpath = By.cssSelector("button.btn.btn-primary");
		element = driver.findElement(byXpath);
		element.click();

		byXpath = By.cssSelector("form#search-form.search-form");
		Assert.assertNotNull(driver.findElement(byXpath));
	}

	// test submitting a comment
	@Test
	public void canComment() {
		driver.get(baseURL);

		By byXpath;
		WebElement element;
		
		byXpath = By.cssSelector("input#edit-name");
		element = driver.findElement(byXpath);
		element.sendKeys("Joe Bloggs");
		
		byXpath = By.cssSelector("input#edit-mail");
		element = driver.findElement(byXpath);
		element.sendKeys("joe@bloggs.com");
		
		byXpath = By.cssSelector("input#edit-homepage");
		element = driver.findElement(byXpath);
		element.sendKeys("http://bloggs.com");

		byXpath = By.cssSelector("textarea#edit-comment-body-und-0-value");
		element = driver.findElement(byXpath);
		element.sendKeys("This is a comment created by the selenium webdriver testing suite.\n\nAnd a bit more on new lines");

		byXpath = By.cssSelector("button.btn.btn-success.form-submit");
		element = driver.findElement(byXpath);
		element.click();
	}
	
	// stuff to do before we start
	@BeforeClass
	public void beforeClass() {
		// Create a new instance of the Firefox driver			 
		driver = new FirefoxDriver();
//		driver = new ChromeDriver();
		
		//Put a Implicit wait, this means that any search for elements on the page could take the time the implicit wait is set for before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		 
	}

	// and stuff to do before we're done
	@AfterClass
	public void afterClass() {
		// close the driver
		driver.quit();
	}
}
