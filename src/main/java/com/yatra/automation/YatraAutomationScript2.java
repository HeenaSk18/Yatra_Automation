package com.yatra.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class YatraAutomationScript2 {

	public static void main(String[] args) throws InterruptedException {
		ChromeOptions chromeOptions= new ChromeOptions();
		chromeOptions.addArguments("--disable-notifications");
		
		
		//step 1 Launch the browser
		WebDriver wd = new ChromeDriver(chromeOptions); //loosely couple 
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20));//after instantenation
		//explicity = synchronize
		//step 2
		wd.get("https://www.yatra.com/");
		
		//Maximize the Browers windows - method chaning
		wd.manage().window().maximize();
		
		By popUpLocator = By.xpath("//div[contains(@class, 'style_popup')][1]");
		try {
		    WebElement popElement = wait.until(ExpectedConditions.visibilityOfElementLocated(popUpLocator));
		    WebElement crossButton = popElement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
		} catch (TimeoutException e) {
		    System.out.println("Pop up not shown the screen!!!");
		}

		
		//Locators ------> By
		//Why dont you create an object of By
		By departureDateButtonLocator = By
				.xpath("//div[@aria-label=\"Departure Date inputbox\"  and  @role=\"button\"]");
//		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20));
		
		//nonsynchroized
		//WebElement departureDateButton = wd.findElement(departureDateButtonLocator);
		//wd - flaky script = synchroized = webdriver wait
			
		WebElement departureDateButton = wait
				.until(ExpectedConditions.elementToBeClickable(departureDateButtonLocator));
		//synchroized = pooling
		
		departureDateButton.click();
		
		By calendarMonthsLocator =By.xpath("//div[@class=\"react-datepicker__month-container\"]");
		
		wd.findElements(calendarMonthsLocator);//list of web element
		
//		List<WebElement> calendarMonthsElement = wd.findElements(calendarMonthsLocator);// non sychronized
		
		List<WebElement> calendarMonthsList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(calendarMonthsLocator));
		//chronozied
		System.out.println(calendarMonthsList.size());
	
		//we want to focus on current month (July)
		
		WebElement julyCalendarWebElement = calendarMonthsList.get(1); //current month
		Thread.sleep(2000);
		
//		By dateLocator = By.xpath(".//div[contains (@class, \"react-datepicker__day\")]");
//		List<WebElement> julyDateList = julyCalendarWebElement.findElements(dateLocator);
//		for(WebElement date : julyDateList)
//		{
//			System.out.println(date.getText());
//		}
		
		By priceLocator = By.xpath(".//span[contains (@class, \"custom-day-content\")]");
		List<WebElement> julyPriceList = julyCalendarWebElement.findElements(priceLocator);
		
		int lowestPrice = Integer.MAX_VALUE;
		WebElement priceElement = null;
		for(WebElement price : julyPriceList)
		{
			
			
//			System.out.println(price.getText());
			//tell which is the lowest price!!

			
			String priceString = price.getText();
			if(priceString.length()>0) {
//			priceString=priceString.replace("₹", "");
//			priceString=priceString.replace(",", "");
			priceString=priceString.replace("₹", "").replace(",", "");
//			System.out.println(priceString);
		
			//Find the smallest Number!!
			//Convert the String value into Integer
			int priceInt = Integer.parseInt(priceString);
			if(priceInt < lowestPrice)
			{
				lowestPrice = priceInt;
				priceElement=price;
			}
			}
		}
		
		System.out.println(lowestPrice);
		WebElement dateElement = priceElement.findElement(By.xpath(".//../.."));
		System.out.println(dateElement.getAttribute("aria-label"));
	
	
	
	
	
	
	
	
	
	}

}
