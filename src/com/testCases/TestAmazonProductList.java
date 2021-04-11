package com.testCases;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.locators.MobilePhones;

public class TestAmazonProductList extends MobilePhones {

	public static void main(String[] args) throws InterruptedException {

		WebDriver driver;

		// Best practice to use System.getProperty("user.dir")
		
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");

		driver = new ChromeDriver();

		driver.manage().window().maximize();

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		driver.get("https://www.amazon.in/");

		driver.findElement(By.xpath(mobileCategory)).click();

		driver.findElement(By.xpath(smartphoneSubCategory)).click();
		
		// Fetch all the mobile phones title till under 1000 catergory
		
		System.out.println("Mobile Phone Available");
		System.out.println("---------------------------------------------------");
		
		List<WebElement> allMobilePhones = driver.findElements(By.xpath(MobilePhones));

		for (WebElement mobiles : allMobilePhones) {

			List<WebElement> allTitles = mobiles.findElements(By.xpath(Titles));

			for (WebElement title : allTitles) {
				
				System.out.println(title.getText());

			}

		}
		
		System.out.println("---------------------------------------------------");
		
		// click on the first mobile phone
		
		driver.findElement(By.xpath(MobilePhones)).click();

		String productAddedToCart = driver.findElement(By.id(productTitle)).getText();

		driver.findElement(By.id(addItemsToCart)).click();

		driver.findElement(By.id(viewCartAnnounce)).click();

		String productTitleInCart = driver.findElement(By.xpath(cartItemTitle)).getText();
		
		// verify if the same product was added to cart
		
		if (productAddedToCart.contains(productTitleInCart)) {

			System.out.println("Product Title Matches");
			System.out.println("Item successfully added to cart");

		} else {

			System.out.println("Product title does not match");
			System.out.println("Check the product which was added to cart");

		}

		Thread.sleep(3000);
		driver.quit();

	}

}
