package acceptance;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.crypto.Data;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SuppressWarnings("NonAsciiCharacters")
public class StepsCollection {

	public static WebDriver driver;

	@Before
	public void beforeScenario() {
		driver = DriverResolver.find();
		// Seems no more working in last Chrome versions
		// driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@After
	public void afterScenario() {
		driver.quit();
	}

	private void waitForLoad(){
		// attend que la page charge
		driver.findElement(By.cssSelector("html:not(.async-hide)"));
	}

	@Given("je suis sur la homepage")
	public void je_suis_sur_la_homepage() {
		driver.get("https://www.tesla.com/fr_FR/");
		waitForLoad();
	}

	@Given("je suis sur la page {string}")
	public void jeSuisSurLaPage(String arg0) {
		driver.get("https://www.tesla.com/fr_FR/" + arg0);
		waitForLoad();
	}

	@When("je clique sur le menu burger")
	public void jeCliqueSurLeMenuBurger() {
		driver.findElement(By.cssSelector("label.tds-menu-header-main--trigger_icon--placeholder")).click();
		WebDriverWait waiter = new WebDriverWait(driver, 20);
		waiter.until(ExpectedConditions.visibilityOfElementLocated(By.id("block-mainheadernavigation")));
	}

	@When("je clique sur le logo tesla")
	public void jeCliqueSurLeLogoTesla() {
		driver.findElement(By.cssSelector("a.tds-site-logo-link")).click();
	}

	@When("je clique sur {string}")
	public void jeCliqueSur(String link){
		WebElement element = driver.findElement(By.xpath("//*[text()='" + link + "']"));
		WebDriverWait waiter = new WebDriverWait(driver, 15);
		waiter.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	@When("je clique sur le lien {string}")
	public void jeCliqueSurLeLien(String link){
		WebElement element = driver.findElement(By.xpath("//a[text()='" + link + "']"));
		WebDriverWait waiter = new WebDriverWait(driver, 15);
		waiter.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	@When("je scroll jusqu'en bas de la page")
	public void jeScrollJusquEnBasDeLaPage() {
		String script =
				"const scroller = document.querySelector('.dialog-off-canvas-main-canvas--with-scroll-snapping');" +
				"scroller.scrollTo(0, scroller.scrollHeight)";
		((JavascriptExecutor)driver).executeScript(script);
	}

	@Then("l'url de la page est {string}")
	public void lUrlDeLaPageEst(String arg0) {
		assertEquals(arg0, driver.getCurrentUrl());
	}

	@Then("le titre doit être {string}")
	public void le_titre_doit_être(String arg1) {
	    assertEquals(arg1, driver.getTitle());
	}

	@Then("la description doit être {string}")
	public void la_description_doit_être(String arg0) {
		assertEquals(arg0, driver.findElement(By.cssSelector("meta[name='description']")).getAttribute("content"));
	}

	//#region page d'accueil

	@Then("il existe les punchlines suivantes")
	public void ilExisteLesPunchlinesSuivantes(@Transpose List<String> args) {
		List<WebElement> headers = driver.findElements(By.cssSelector("h1.tcl-hero-parallax__heading"));
		for (int i = 0; i < args.size(); i++)
			assertEquals(args.get(i), headers.get(i).getText());
	}


	@Then("le menu du haut contient les liens suivants")
	public void leMenuDuHautContientLesLiensSuivants(DataTable links) {
		List<WebElement> nav = driver.findElements(By.cssSelector("#block-mainheadernavigation > ol > li > a"));
		for (int i = 0; i < links.height(); i++) {
			assertEquals(links.cell(i, 0).toLowerCase(), nav.get(i).getText().toLowerCase());
			assertEquals(links.cell(i, 1), nav.get(i).getAttribute("href"));
		}
	}

	@Then("le burger menu contient les liens suivants")
	public void leBurgerMenuContientLesLiensSuivants(List<String> links) {
		WebDriverWait waiter = new WebDriverWait(driver, 5);
		waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#block-hamburgerdesktop > ol > li > a")));
		List<WebElement> nav = driver.findElements(By.cssSelector("#block-hamburgerdesktop > ol > li > a"));
		for (String link : links) {
			assertTrue(link, nav.stream().anyMatch(a -> a.getText().equalsIgnoreCase(link)));
		}
	}

	//#endregion

	//#region configurateur

	@Then("le prix affiché est un prix {string} de {string}€ par mois")
	public void lePrixAffichéEstUnPrixDe€ParMois(String arg0, String arg1) {
		assertEquals(arg0, driver.findElement(By.cssSelector("span.pricing-label")).getText());
		assertTrue("prix", driver.findElement(By.cssSelector("span.finance-type")).getText().contains(arg1));
	}

	@Then("l'économie de carburant est de {string}€ par mois")
	public void lÉconomieDeCarburantEstDe€ParMois(String arg0) {
		System.out.println(driver.findElement(By.cssSelector(".summary-panel--item > span.finance-type--lease")).getText());
		assertTrue("économie carburant", driver.findElement(By.cssSelector(".summary-panel--item.tds-text--caption > span.finance-type--lease")).getText().contains(arg0));
	}

	@Then("le prix total est de {string}€")
	public void lePrixTotalEstDe€(String arg0) throws InterruptedException {
		driver.findElement(By.cssSelector(".footer-action-block button.modal-trigger")).click();
		jeCliqueSur("Comptant");

		String price = arg0 + " €";
		assertEquals(price, driver.findElement(By.cssSelector(".financial--highlighted-summary thead th.line-item--value")).getText());

		jeCliqueSur("LOA");
		driver.findElement(By.cssSelector("button.tds-modal-close")).click();

		WebDriverWait waiter = new WebDriverWait(driver, 5);
		waiter.until(ExpectedConditions.invisibilityOfElementLocated(By.id("tds-main-modal")));
	}

	@When("j'ajoute l'option de conduite totalement automatique")
	public void jAjouteLOptionDeConduiteTotalementAutomatique() {
		driver.findElement(By.cssSelector(".child-group--option_details div:nth-of-type(6) button")).click();
	}

	//#endregion

	//#region configurateur model 3

	@Then("le poids est de {string} kg")
	public void lePoidsEstDeKg(String arg0) {
		String weight = arg0 + " kg";
		List<WebElement> elements = driver.findElements(By.xpath("//*[text()='Poids']/../span[2]"));
		WebElement current = elements.stream().filter(WebElement::isDisplayed).findFirst().orElseThrow();
		assertEquals(weight, current.getText());
	}

	@Then("l'accélération est de {string}s")
	public void lAccélérationEstDeS(String arg0) {
		List<WebElement> elements = driver.findElements(By.xpath("//*[text()='Accélération']/.."));
		WebElement current = elements.stream().filter(WebElement::isDisplayed).findFirst().orElseThrow();
		assertTrue("acceleration", current.getText().contains(arg0));
	}

	@Then("l'autonomie est de {string} km")
	public void lAutonomieEstDeKm(String arg0) {
		String autonomy = arg0 + " km";
		List<WebElement> elements = driver.findElements(By.xpath("//*[(text()='Autonomie (WLTP)') or (text()='Autonomie')]/../span[2]"));
		WebElement current = elements.stream().filter(WebElement::isDisplayed).findFirst().orElseThrow();
		assertEquals(autonomy, current.getText());
	}

	@When("je clique sur l'onglet {string}")
	public void jeCliqueSurLOnglet(String arg0) {
		driver.findElement(By.cssSelector("li.side_nav-item[data-title=\"" + arg0.toLowerCase() + "\"]")).click();
	}

	//#endregion

	//#region événements

	@Then("il y a {int} événements affichés")
	public void ilYAÉvénementsAffichés(int arg0) {
		assertEquals(arg0, driver.findElements(By.cssSelector(".pane-events-page .views-row")).size());
	}

	@Then("il existe un lien {string}")
	public void ilExisteUnLien(String arg0) {
		assertFalse(arg0, driver.findElements(By.xpath("//a[text()='" + arg0 + "']")).isEmpty());
	}

	@Then("il existe les champs suivants")
	public void ilExisteLesChampsSuivants(@Transpose List<String> args) {
		List<WebElement> labels = driver.findElements(By.cssSelector("label[for]"));
		for (String field: args)
			assertTrue(field, labels.stream().anyMatch(el -> el.getText().startsWith(field)));
	}

	@And("il existe un bouton {string}")
	public void ilExisteUnBouton(String arg0) {
		assertFalse(arg0, driver.findElements(By.cssSelector("input[type=\"submit\"][value=\"" + arg0 + "\"]")).isEmpty());
	}

	@When("je remplis le formulaire avec les valeurs suivantes")
	public void jeRemplisLeFormulaireAvecLesValeursSuivantes(DataTable values) {
		List<WebElement> labels = driver.findElements(By.cssSelector("label[for]"));
		for (int i = 0; i < values.height(); i++) {
			int finalI = i;
			WebElement label = labels.stream().filter(el -> el.getText().startsWith(values.cell(finalI, 1))).findFirst().orElseThrow();
			WebElement input = driver.findElement(By.id(label.getAttribute("for")));
			input.sendKeys(Keys.CLEAR);
			input.sendKeys(Keys.DELETE);
			input.sendKeys(values.cell(i, 0));
		}
	}

	@When("je valide le formulaire")
	public void jeValideLeFormulaire() {
		driver.switchTo().activeElement().sendKeys(Keys.RETURN);
	}

	@Then("un message {string} apparait pour le champ {string}")
	public void unMessageApparaitPourLeChamp(String arg0, String arg1) {
		assertFalse(arg0, driver.findElements(By.xpath("//label[starts-with(text(),'" + arg1 + "')]/..//li[text()='" + arg0 + "']")).isEmpty());
	}

	@Then("le premier événement est au {string}")
	public void lePremierÉvénementEstAu(String arg0) {
		WebDriverWait waiter = new WebDriverWait(driver, 5);
		waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".location-teaser")));
		assertEquals(arg0, driver.findElement(By.cssSelector(".location-teaser")).getText());
	}

	@And("j'attend que {string} soit visible")
	public void jAttendQueSoitVisible(String arg0) {
		WebDriverWait waiter = new WebDriverWait(driver, 5);
		waiter.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(arg0)));
	}

	//#endregion

}
