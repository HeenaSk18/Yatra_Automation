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

public class YatraAutomationScript3 {
	public static void main(String[] args) throws InterruptedException {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--disable-notifications");

		// Step 1: Launch the browser!
		WebDriver wd = new ChromeDriver(chromeOptions);
		WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(20));// synchronizing the webdriver!

		// Step 2: //load the page for us
		wd.get("https://www.yatra.com");

		// Maximize the Browser Window
		wd.manage().window().maximize();

		// Check for the popup!!!
		By popUpLocator = By.xpath("//div[contains(@class, \"style_popup\")][1]");

		try {
			WebElement popUpElement = wait.until(ExpectedConditions.visibilityOfElementLocated(popUpLocator));
			WebElement crossButton = popUpElement.findElement(By.xpath(".//img[@alt=\"cross\"]"));
			crossButton.click();
		}

		catch (TimeoutException e) {
			System.out.println("Pop up not shown the screen!!!");
		}

		// Locators-----> By
		// Why dont you create an object of By
		By departureDateButtonLocator = By
				.xpath("//div[@aria-label=\"Departure Date inputbox\"  and @role=\"button\"]");

		// WebElement departureDateButton = wd.findElement(departureDateButtonLocator);
		WebElement departureDateButton = wait
				.until(ExpectedConditions.elementToBeClickable(departureDateButtonLocator));

		departureDateButton.click();

		// List<WebElement> calendarMonthsWebElement =
		// wd.findElements(calendarMonthsLocator);//??

		WebElement currentMonthWebElement = selectTheMonthFromCalendar(wait, 0); // current Month
		WebElement nextMonthWebElement = selectTheMonthFromCalendar(wait, 1); // Next Month

		Thread.sleep(3000);
		String lowestPriceForCurrentMonth = getMeLowestPrice(currentMonthWebElement);
		String lowestPriceForNextMonth = getMeLowestPrice(nextMonthWebElement);
		System.out.println(lowestPriceForCurrentMonth);
		System.out.println(lowestPriceForNextMonth);
		compareTwoMonthsPrices(lowestPriceForCurrentMonth, lowestPriceForNextMonth);
	}

	public static String getMeLowestPrice(WebElement monthWebElement) {
		By priceLocator = By.xpath(".//span[contains(@class,\"custom-day-content\")]");
		List<WebElement> junePriceList = monthWebElement.findElements(priceLocator);

		int lowestPrice = Integer.MAX_VALUE;
		WebElement priceElement = null;
		for (WebElement price : junePriceList) {

			// Tell which is the lowest price!!
			String priceString = price.getText();
			if (priceString.length() > 0) {
				priceString = priceString.replace("₹", "").replace(",", "");
				// System.out.println(priceString);
				// Find the smallest Number!!
				// Convert the String value into Integer
				int priceInt = Integer.parseInt(priceString);
				if (priceInt < lowestPrice) {
					lowestPrice = priceInt;
					priceElement = price;
				}
			}
		}

		WebElement dateElement = priceElement.findElement(By.xpath(".//../.."));

		String result = dateElement.getAttribute("aria-label") + "--- Price is Rs" + lowestPrice;
		return result;
	}

	public static WebElement selectTheMonthFromCalendar(WebDriverWait wait, int index) {
		By calendarMonthsLocator = By.xpath("//div[@class=\"react-datepicker__month-container\"]");
		List<WebElement> calendarMonthsList = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(calendarMonthsLocator));
		System.out.println(calendarMonthsList.size());
		WebElement monthCalendarWebElement = calendarMonthsList.get(index);
		return monthCalendarWebElement;
	}

	public static void compareTwoMonthsPrices(String currentMonthPrice, String nextMonthPrice) {

		int currentMonthRSIndex = currentMonthPrice.indexOf("Rs");
		int nextMonthRSIndex = nextMonthPrice.indexOf("Rs");

		String currentPrice = currentMonthPrice.substring(currentMonthRSIndex + 2);
		String nextPrice = nextMonthPrice.substring(nextMonthRSIndex + 2);

		int current = Integer.parseInt(currentPrice);
		int next = Integer.parseInt(nextPrice);

		if (current < next) {
			System.out.println("The lowest price for the two months is" + current);
		}

		else if (current == next) {
			System.out.println("Price is same for both months! Choose whatever you prefer!!");

		}

		else {
			System.out.println("The lowest price for the two months is" + next);

		}

	}

}