package com.testCases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.db_connection.DBConnection;
import com.locators.MobilePhones;

public class TestAmazonProductList extends MobilePhones {

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, SQLException {

		WebDriver driver;
		
		FileReader reader = new FileReader(new File(System.getProperty("user.dir") + "/src/db_config.properties"));
		Properties props = new Properties();
		props.load(reader);
		
		DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
		Statement stmt = conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		try {

			stmt.executeUpdate("create database orders");
			System.out.println("Created Database : orders");
			System.out.println(" -------------------------------------- ");

		} catch (SQLException e) {

			System.out.println("Database: orders already exists ");
			System.out.println(" -------------------------------------- ");
			e.printStackTrace();

		}
		
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
		
		String item_name = (String) productTitleInCart.split("-")[0];
		
		String cartValue = (String) driver.findElement(By.xpath(totalAmount)).getText();
		
		stmt.executeUpdate("use orders");
		System.out.println("Selected Database : orders");
		System.out.println(" -------------------------------------- ");

		stmt.executeUpdate("create table orders_received (ID int auto_increment primary key, NAME varchar(250), PRICE varchar(250), DATE_ADDED timestamp default now())");
		System.out.println("Created Table : orders_received");
		System.out.println(" -------------------------------------- ");

		stmt.executeUpdate("insert into orders_received (NAME, PRICE) values ('" + item_name.trim() + " ', '" + cartValue.trim() + "')");
		System.out.println("Executed Insert Operation ");
		System.out.println(" -------------------------------------- ");

		ResultSet rst = stmt.executeQuery("select * from orders_received");
		while (rst.next()) {
			System.out.println(rst.getInt("ID") + " --- " + rst.getString("NAME").trim() + " --- " + rst.getString("PRICE") + " --- " + rst.getString("DATE_ADDED"));
		}
		System.out.println(" --------------------------------------------------- ");
		
		stmt.executeUpdate("drop table orders_received");
		System.out.println("Dropped Table : orders_received");
		System.out.println(" -------------------------------------- ");

		stmt.executeUpdate("drop database orders");
		System.out.println("Dropped Database : orders");
		System.out.println(" -------------------------------------- ");
		
		// verify if the same product was added to cart
		
		if (productAddedToCart.contains(productTitleInCart)) {

			System.out.println("Product Title Matches");
			System.out.println("Item successfully added to cart");

		} else {

			System.out.println("Product title does not match");
			System.out.println("Check the product which was added to cart");

		}
		
		stmt.close();
		conn.closeConnection();
		reader.close();
		
		Thread.sleep(3000);
		driver.quit();

	}

}
