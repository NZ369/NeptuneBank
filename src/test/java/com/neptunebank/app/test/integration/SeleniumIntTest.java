package com.neptunebank.app.test.integration;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static java.lang.Math.abs;

@Tag("Integration")
public class SeleniumIntTest {
	private WebDriver driver;
	WebDriverWait wait;
	private String url;

	private final String validUsername = "lpaprocki";
	private final String validPassword = "feltzprintingservice";

	private final String userUsername = "jbutt", userPassword = "bentonjohnbjr";
	private final String adminUser = "pcosta", adminPassword = "dragonballz1";
	private final String managerUser = "jbonano", managerPassword = "30secondstomars";

	@BeforeEach
	public void setUp() {
		//default to chromedriver
		String browser = System.getProperty("integrationTest.browser", "chrome");
		setDriver(browser);
		url = "http://localhost:4080";
		driver.get(url);
		wait = new WebDriverWait(driver, 40);
	}

	private void login() {
		login(validUsername, validPassword);
	}

	// Login function for a customer account
	private void login(String username, String password) {
		driver.findElement(By.cssSelector("nav.navbar li.nav-item > a#login-item")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#login-page form"));

		formEl.findElement(By.id("username")).sendKeys(username);
		formEl.findElement(By.id("password")).sendKeys(password);

		formEl.findElement(By.cssSelector("button[type=submit]")).click();
	}

	// Login function for an admin account
	private void loginAdmin() {
		String username = "pcosta";
		String password = "dragonballz1";
		driver.findElement(By.cssSelector("nav.navbar li.nav-item > a#login-item")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#login-page form"));

		formEl.findElement(By.id("username")).sendKeys(username);
		formEl.findElement(By.id("password")).sendKeys(password);

		formEl.findElement(By.cssSelector("button[type=submit]")).click();
	}

	// Login function for an admin account
	private void loginManager() {
		String username = "ecressvan";
		String password = "betar@m0s";
		driver.findElement(By.cssSelector("nav.navbar li.nav-item > a#login-item")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#login-page form"));

		formEl.findElement(By.id("username")).sendKeys(username);
		formEl.findElement(By.id("password")).sendKeys(password);

		formEl.findElement(By.cssSelector("button[type=submit]")).click();
	}

	// Creates a payee for testing pruposes
	private void testingPayee() {
		String firstName = "PayeeForTesting";
		String lastName = RandomStringUtils.randomAlphanumeric(8);
		String email = (firstName + "@" + lastName + ".mail").toLowerCase();
		String telephone = RandomStringUtils.randomNumeric(10);

		driver.findElement(By.linkText("Manage Payees")).click();

		driver.findElement(By.id("jh-create-entity")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#app-view-container form"));

		formEl.findElement(By.id("payee-emailID")).sendKeys(email);
		formEl.findElement(By.id("payee-firstName")).sendKeys(firstName);
		formEl.findElement(By.id("payee-lastName")).sendKeys(lastName);
		formEl.findElement(By.id("payee-telephone")).sendKeys(telephone);

		formEl.submit();

		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

		driver.findElement(By.cssSelector("#header-tabs > li:nth-child(1) > a")).click();
	}

	@Test
	public void headerIsCorrect() throws Exception {
		assertEquals("Neptune Bank", driver.getTitle());
	}

	// User Story #2: Sign In
	@Test
	public void logsUserWithCorrectCredentials() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login(validUsername, validPassword);
		assertTrue(isElementPresent(By.id("account-menu")));
	}

	// Invalid sign in attempt
	@Test
	public void doesNotLogUserWithIncorrectCredentials() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login(validUsername, validPassword + "toMakeInvalid");
		assertFalse(isElementPresent(By.id("account-menu")));
	}

	@Test
	// User Story #6: Adding a new payee
	public void payeeIsAddedCorrectly() {
		String firstName = "Lola";
		String lastName = RandomStringUtils.randomAlphanumeric(8);
		String email = (firstName + "@" + lastName + ".mail").toLowerCase();
		String telephone = RandomStringUtils.randomNumeric(10);

		login();
		driver.findElement(By.linkText("Manage Payees")).click();

		driver.findElement(By.id("jh-create-entity")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#app-view-container form"));

		formEl.findElement(By.id("payee-emailID")).sendKeys(email);
		formEl.findElement(By.id("payee-firstName")).sendKeys(firstName);
		formEl.findElement(By.id("payee-lastName")).sendKeys(lastName);
		formEl.findElement(By.id("payee-telephone")).sendKeys(telephone);

		formEl.submit();

		//after creation ensure the payee is in the list

		WebElement tableEl = driver.findElement(By.cssSelector("#app-view-container table"));
		assertTrue(isElementPresent(tableEl, By.xpath(".//td[text()='" + email + "']")), "Email of new payee not found in table");
	}

	// User Story #1: Register a User
	@Test // New user created successfully
	public void registerUserValid() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		driver.findElement(By.cssSelector("#header-tabs > li:nth-child(3) > a")).click();

		String name = RandomStringUtils.randomAlphanumeric(8);
		String email = (name + "@gmail.com").toLowerCase();

		driver.findElement(By.id("username")).sendKeys(name);
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("firstPassword")).sendKeys("1234apples");
		driver.findElement(By.id("secondPassword")).sendKeys("1234apples");

		driver.findElement(By.id("register-submit")).click();

		assertTrue(isElementPresent(By.xpath("//*[contains(text(),'Your account has successfully been created.')]")));
	}

	@Test // Username already taken
	public void registerUserInvalid() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		driver.findElement(By.cssSelector("#header-tabs > li:nth-child(3) > a")).click();

		driver.findElement(By.id("username")).sendKeys(validUsername);
		driver.findElement(By.id("email")).sendKeys("invalid@gmail.com");
		driver.findElement(By.id("firstPassword")).sendKeys("1234apples");
		driver.findElement(By.id("secondPassword")).sendKeys("1234apples");

		driver.findElement(By.id("register-submit")).click();

		assertTrue(isElementPresent(By.xpath("//*[contains(text(),'Login name already used!')]")));
	}

	// User Story #4: Contact Us Page
	@Test
	public void contactUsPage() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		driver.findElement(By.linkText("Contact Us")).click();
		assertTrue(isElementPresent(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div.col-md-9 > h2")));
	}

	// User Story #5: About Us
	@Test
	public void aboutUsPage() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		driver.findElement(By.cssSelector("#header-tabs > li:nth-child(4) > a")).click();
		assertTrue(isElementPresent(By.xpath("//*[contains(text(),'Neptune Bank at a glance')]")));
	}

	// User Story #7: Updating payee information
	@Test
	public void updatingPayeeInfo() {
		// Updates payee telephone number
		String telephone = "2507994567";
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login();
		//testingPayee();
		driver.findElement(By.linkText("Manage Payees")).click();
		driver.findElement(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div > div.table-responsive > div > table > tbody > tr:nth-child(1) > td.text-right > div > a.btn.btn-primary.btn-sm")).click();
		assertTrue(isElementPresent(By.xpath("//*[contains(text(),'Create or edit a Payee')]")));
		driver.findElement(By.id("payee-telephone")).clear();
		driver.findElement(By.id("payee-telephone")).sendKeys(telephone);
		driver.findElement(By.id("save-entity")).click();

		// Verify that the new changes were registered
		String currNum = driver.findElement(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div > div.table-responsive > div > table > tbody > tr:nth-child(1) > td:nth-child(5)")).getText();
		assertEquals(currNum, telephone);
	}

	// User Story #8: View information about a payee
	@Test
	public void viewPayeeInfo() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login();
		//testingPayee();
		driver.findElement(By.linkText("Manage Payees")).click();
		driver.findElement(By.xpath("//*[text() = 'View']")).click();
		assertTrue(isElementPresent(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div > div > dl")));
	}

	// User Story #9: Deleting a payee
	@Test
	public void deletingPayee() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login();
		//testingPayee();
		driver.findElement(By.linkText("Manage Payees")).click();
		// Get number of payees listed
		int sizeBefore = driver.findElements(By.xpath("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();
		// Deletes the payee
		driver.findElement(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div > div.table-responsive > div > table > tbody > tr:nth-child(1) > td.text-right > div > a.btn.btn-danger.btn-sm")).click();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		driver.findElement(By.id("jhi-confirm-delete-payee")).click();
		// Get number of current payees listed
		int sizeAfter = driver.findElements(By.xpath("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();
		int res = sizeBefore - sizeAfter;
		assertEquals(res, 1);
	}

	// User Story #10: Updating a customer’s information through manager account
	@Test
	public void updatingCustomerInfo() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		loginManager();
		driver.findElement(By.linkText("Banking")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Customer Details")).click();
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/div/table/tbody/tr[1]/td[11]/div/a[2]")).click();

		WebElement formEl = driver.findElement(By.cssSelector("#app-view-container > div.jh-card.card > div > div > div > div:nth-child(2) > div > form"));
		String oldLastName = formEl.findElement(By.id("customer-lastName")).getText();
		formEl.findElement(By.id("customer-lastName")).clear();
		formEl.findElement(By.id("customer-lastName")).sendKeys("NewLastname");
		formEl.submit();

		String newLastName = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/div/table/tbody/tr[1]/td[3]")).getText();
		assertNotEquals(newLastName, oldLastName);
	}

	// User Story #11: Deleting a customer’s information through manager account
	@Test
	public void deletingCustomerInfo() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		loginManager();
		driver.findElement(By.linkText("Banking")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Customer Details")).click();
		String userNumber = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/div/table/tbody/tr[1]/td[4]")).getText();
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/div/table/tbody/tr[1]/td[11]/div/a[3]")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.id("jhi-confirm-delete-customer")).click();
		// Not completed due to bug
	}

	// User story #20: Upload documents as a user or admin
	@Test
	public void uploadingDocument() {
		// Note that '/' is for macs, change path string for windows
		String uploadPath = System.getProperty("user.dir")+"/src/test/java/com/neptunebank/app/test/integration/test.txt";
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		loginAdmin();
		driver.findElement(By.linkText("Banking")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Document Upload")).click();
		WebElement chooseFile = driver.findElement(By.id("file_file"));
		chooseFile.sendKeys(uploadPath);
		driver.findElement(By.xpath("//*[@id=\"save-entity\"]")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		assertTrue(isElementPresent(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div")));
	}

	// User story #21: Delete the uploaded document before saving
	@Test
	public void deletingDocument() {
		String uploadPath = System.getProperty("user.dir")+"/src/test/java/com/neptunebank/app/test/integration/test.txt";
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		loginAdmin();
		driver.findElement(By.linkText("Banking")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Document Upload")).click();
		WebElement chooseFile = driver.findElement(By.id("file_file"));
		chooseFile.sendKeys(uploadPath);
		driver.findElement(By.xpath("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[2]/div/form/div/div/div/div/div[2]/button")).click();
	}

	// User story #23: Sort the customer details table using the filters in the table User Management
	@Test
	public void sortFilter() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		loginAdmin();
		driver.findElement(By.linkText("Administration")).click();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(By.linkText("User management")).click();
		// Check ascending filter and descending filter
		int m = 0;
		while (m < 2) {
			driver.findElement(By.xpath("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/table/thead/tr/th[1]")).click();
			String[] ids = new String[5];
			ids[0] = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/table/tbody/tr[1]/td[1]/a")).getText();
			ids[1] = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/table/tbody/tr[2]/td[1]/a")).getText();
			ids[2] = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/table/tbody/tr[3]/td[1]/a")).getText();
			ids[3] = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/table/tbody/tr[4]/td[1]/a")).getText();
			ids[4] = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[3]/div[1]/div/div/div/div[1]/table/tbody/tr[5]/td[1]/a")).getText();
			for (int i = 0; i < 4; i++) {
				int id1 = Integer.parseInt(ids[i]);
				int id2 = Integer.parseInt(ids[i + 1]);
				int res = abs(id1 - id2);
				assertEquals(res, 1);
			}
			m++;
		}
	}



	//Feature 6: Administrative Functionality
	//User Story #12: Monitor system stats as Admin
	@Test
	public void navigateToApplicationMetrics(){
		login(adminUser,adminPassword);
		driver.findElement(By.id("admin-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"admin-menu\"]/div/a[2]")).click();
		assertTrue(isElementPresent(By.id("metrics-page-heading")));
	}


	//User Story #13: Tweak the system’s Database settings
	@Test
	public void navigateToDatabase() {
		login(adminUser,adminPassword);
		driver.findElement(By.id("admin-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"admin-menu\"]/div/a[8]")).click();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1)); //Switch to H2 Console tab
		assertTrue(isElementPresent(By.className("login"))); //Confirm redirected to database log-in page
	}

	//User Story #14: Manage Users
	@Test
	public void manageUserAccounts() {
		login(adminUser,adminPassword);
		driver.findElement(By.id("admin-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"admin-menu\"]/div/a[1]")).click();
		assertTrue(isElementPresent(By.id("user-management-page-heading")));
	}

	//User Story #16: Transfer funds between accounts as Admin
	@Test
	public void navigateToTransferMoneyPage() {
		login(adminUser,adminPassword);
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[3]")).click();
		driver.findElement(By.id("jh-create-entity")).click();
		assertTrue(isElementPresent(By.id("neptunebank.transaction.home.createOrEditLabel")));
	}

	//User Story #17: View transaction history of all users as Admin
	@Test
	public void navigateToTransactionPage() {
		login(adminUser,adminPassword);
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[3]")).click();
		assertTrue(isElementPresent(By.id("transaction-heading")));
	}


	//Feature 7
	//User Story #18: Create a new branch as Manager
	@Test
	public void createBranch() throws InterruptedException {

		int row_count,newRow_count; //number of branches in the table


		login(managerUser,managerPassword);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("#header-tabs > li:nth-child(2) > a")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table-responsive")));
		row_count = driver.findElements(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();
		driver.findElement(By.id("jh-create-entity")).click();
		driver.findElement(By.id("branch-address")).sendKeys("3755  Reserve St");
		driver.findElement(By.id("branch-city")).sendKeys("Victoria");
		driver.findElement(By.id("branch-state")).sendKeys("BC");
		driver.findElement(By.id("branch-pinCode")).sendKeys("58290");
		driver.findElement(By.cssSelector("button[type=submit]")).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table-responsive")));
		newRow_count = driver.findElements(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();
		assertEquals(row_count+1,newRow_count);
		deleteData();
	}

	//User Story #19: Editing Accounts For Approval
	@Test
	public void editAccount() throws InterruptedException {
		String accountType,balance,customer,branch,
			newAccountType,newBalance,newCustomer,newBranch;

		login(managerUser,managerPassword);
		driver.findElement(By.linkText("Account Approval")).click();
		driver.findElement(By.xpath("//div[1]/div/div/div[1]/div/table/tbody/tr[last()]/td[7]/div/a[2]")).click(); //Edit the account in the last row
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("col-md-8")));

		Select actTypDrp = new Select(driver.findElement(By.name("accountType")));
		Select cidDrp = new Select(driver.findElement(By.name("customerID")));
		Select bidDrp = new Select(driver.findElement(By.name("branchID")));

		//Save current data values for undoing changes after test
		accountType = actTypDrp.getFirstSelectedOption().getText();
		balance = driver.findElement(By.id("accounts-balance")).getAttribute("value");
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		customer = cidDrp.getFirstSelectedOption().getText();
		branch = bidDrp.getFirstSelectedOption().getText();

		//Edit values
		actTypDrp.selectByVisibleText("Checking");
		driver.findElement(By.id("accounts-balance")).clear();
		driver.findElement(By.id("accounts-balance")).sendKeys("600");
		cidDrp.selectByVisibleText("echui");
		bidDrp.selectByVisibleText("4 Berrard pl");

		driver.findElement(By.cssSelector("button[type=submit]")).click();

		//verify values have been changed
		wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table-responsive")));
		newAccountType = driver.findElement(By.xpath("(//div[1]/div/div/div[1]/div/table/tbody/tr)[last()]/td[2]")).getText();
		newBalance = driver.findElement(By.xpath("(//div[1]/div/div/div[1]/div/table/tbody/tr)[last()]/td[3]")).getText();
		newCustomer = driver.findElement(By.xpath("(//div[1]/div/div/div[1]/div/table/tbody/tr)[last()]/td[5]")).getText();
		newBranch = driver.findElement(By.xpath("(//div[1]/div/div/div[1]/div/table/tbody/tr)[last()]/td[6]")).getText();

		assertEquals(newAccountType,"Checking");
		assertEquals(newBalance,"600");
		assertEquals(newCustomer,"echui");
		assertEquals(newBranch,"4 Berrard pl");

		//Restore original values after test
		restoreVal(accountType,balance,customer,branch);
	}

	//User Story 15: User Transferring Money
	//Note: The next 3 tests are made to cover variations of US 15

	//Verify the money transfer made by the user can be seen in the transactions table
	@Test
	public void verifyMoneyTransfer() {

		int row_size;
		String fromAcc = "1000001",toAccount1 = "1000004", toAccount2 = "1000008";
		String toAccountIdVerifier, transferAmountVerifier;
		double transferAmount = 1.0;
		Double bal;

		login(userUsername,userPassword);
		bal = getBalance(fromAcc);
		assertNotNull(bal);
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[3]")).click();

		transferMoneyTo(toAccount1,fromAcc,transferAmount);
		transferMoneyTo(toAccount2,fromAcc,transferAmount);

		row_size = driver.findElements(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();

		//verify first transaction
		toAccountIdVerifier = driver.findElement(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr["+(row_size-1)+"]/td[5]")).getText();
		transferAmountVerifier = driver.findElement(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr["+(row_size-1)+"]/td[3]")).getText();
		assertEquals(toAccount1,toAccountIdVerifier);
		assertEquals(transferAmount,Double.parseDouble(transferAmountVerifier));

		//verify second transaction
		toAccountIdVerifier = driver.findElement(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr[last()]/td[5]")).getText();
		transferAmountVerifier = driver.findElement(By.xpath("//div[1]/div/div/div/div[1]/div/table/tbody/tr[last()]/td[3]")).getText();
		assertEquals(toAccount2,toAccountIdVerifier);
		assertEquals(transferAmount,Double.parseDouble(transferAmountVerifier));
	}

	/**
	 * Test how the application responds if the user tries to transfer money to an invalid account ID.
	 */
	@Test
	public void invalidToAccountId(){
		login(userUsername, userPassword);
		String fromAcc = "1000002", invalidToAcc = "123";
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[3]")).click();
		transferMoneyTo(invalidToAcc,fromAcc,10.0);
		assertTrue(isElementPresent(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div/div[1][contains(text()," +
			"'Account detail could not be found: "+invalidToAcc+"')]")));
	}

	/**
	 * Test how the application responds if the user tries to transfer money exceeding the user's balance.
	 */
	@Test
	public void notEnoughBalance(){
		login(userUsername, userPassword);
		String fromAcc = "1000002", toAcc = "1000001";
		Double bal = getBalance(fromAcc);
		assertNotNull(bal);
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[3]")).click();
		transferMoneyTo(toAcc,fromAcc,bal+10);
		assertTrue(isElementPresent(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div/div/div[contains(text()," +
			"'Insufficient funds to process this transaction')]")));
	}

	/**
	 * Transfer money from one account to another
	 * @param recipientAccNum The account number of the receiver
	 * @param fromAcc The account number of the sender
	 * @param amount The amount of money to be sent
	 */
	private void transferMoneyTo(String recipientAccNum, String fromAcc,double amount){
		//assert getBalance()
		driver.findElement((By.id("jh-create-entity"))).click();
		driver.findElement(By.name("amount")).sendKeys(Double.toString(amount));
		driver.findElement(By.name("toAccount1")).sendKeys(recipientAccNum);
		Select fromAccDrp = new Select(driver.findElement(By.name("fromAccount")));
		fromAccDrp.selectByVisibleText(fromAcc);
		driver.findElement(By.cssSelector("button[type=submit]")).click();
	}

	/**
	 * Gets the current balance of the account number specified.
	 * @param fromAcc The account number to be checked
	 * @return Returns the balance of the specified account
	 */
	private Double getBalance(String fromAcc){
		Double balance = null;
		int row_size;
		String row_accId;
		driver.findElement(By.id("entity-menu")).click();
		driver.findElement(By.xpath("//*[@id=\"entity-menu\"]/div/a[2]")).click();

		row_size = driver.findElements(By.xpath
			("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/div/table/tbody/tr")).size();
		for(int i=1; i<=row_size; i++) {
			row_accId = driver.findElement(By.xpath
				("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/div/table/tbody/tr[" + i +"]/td[1]")).getText();
			if (row_accId.equals(fromAcc)) {
				balance = Double.parseDouble(driver.findElement(By.xpath
					("//*[@id=\"app-view-container\"]/div[1]/div/div/div/div[1]/div/table/tbody/tr[" + i+"]/td[3]")).getText());
			}
		}
		return balance;
	}

	/**
	 * Deletes recent(last row) data created by tests
	 */
	private void deleteData(){
		driver.findElement(By.xpath("(//td)[last()]/div/a[3]")).click(); //Click delete button
		WebElement deleteConfirmation = driver.findElement(By.className("modal-content"));
		deleteConfirmation.findElement(By.cssSelector("button[class=\"btn btn-danger\"]")).click(); //Confirm deletion
	}

	/**
	 * Restores the values before the test edits were made
	 * @param aType Customer account type
	 * @param balance Customer account balance
	 * @param customer Customer name
	 * @param branch Customer account branch
	 */
	private void restoreVal(String aType,String balance,String customer,String branch){
		driver.findElement(By.xpath("//div[1]/div/div/div[1]/div/table/tbody/tr[last()]/td[7]/div/a[2]")).click();

		Select actTypDrp = new Select(driver.findElement(By.name("accountType")));
		Select cidDrp = new Select(driver.findElement(By.name("customerID")));
		Select bidDrp = new Select(driver.findElement(By.name("branchID")));

		actTypDrp.selectByVisibleText(aType);
		driver.findElement(By.id("accounts-balance")).clear();
		driver.findElement(By.id("accounts-balance")).sendKeys(balance);
		cidDrp.selectByVisibleText(customer);
		bidDrp.selectByVisibleText(branch);

		driver.findElement(By.cssSelector("button[type=submit]")).click();
	}


	@AfterEach
	public void tearDown() {
		driver.quit();
	}

	private boolean isElementPresent(By by) {
		return isElementPresent(driver, by);
	}

	private boolean isElementPresent(SearchContext context, By by) {
		try {
			context.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void setDriver(String driverName) {
		driverName = driverName.toLowerCase();

		long timeout = 5;

		switch (driverName) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "/opt/geckodriver");
				driver = new FirefoxDriver();
				driver.manage().window().maximize();
				break;
			case "chrome":
				//**IMPORTANT: change the chromedriver path below to your specific machine.**
				System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver");
				ChromeOptions options = new ChromeOptions();

				//run in headless mode for Jenkins
				//options.addArguments("--headless");
				options.addArguments("--start-maximized");
				options.addArguments("--whitelisted-ips=\"\"");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("--ignore-ssl-errors");
				driver = new ChromeDriver(options);
				break;
			default:
				driver = new HtmlUnitDriver();
				((HtmlUnitDriver) driver).setJavascriptEnabled(true);
				timeout = 20; // HtmlUnitDriver is slower than Firefox and Chrome
		}

		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
	}
}
