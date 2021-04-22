package functional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FunctionalTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        driver = DriverResolver.find();
        // Seems no more working in last Chrome versions
        // driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    // Test de la Story #1-homepage (https://trello.com/c/WKTneu9o/1-homepage)
    @Test
    public void testHomepage() {
        driver.get("https://www.meetup.com/fr-FR/");
        // Le titre de la page d'accueil doit être: Partagez vos passions | Meetup
        assertEquals("Partagez vos passions | Meetup", driver.getTitle());

        // La description de la page d'accueil doit être: "Partagez vos passions et faites bouger votre ville ! Meetup vous aide...
        assertEquals("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt.",
                driver.findElement(By.cssSelector("meta[name=\"description\"]")).getAttribute("content"));

        // La punchline (h1) du site doit être: Le monde vous tend les bras
        assertEquals("Le monde vous tend les bras", driver.findElement(By.cssSelector("h1.exploreHome-hero-mainTitle")).getText());

        // La sous punchline doit être: Rejoignez un groupe local pour rencontrer du monde, tester une nouvelle activité ou partager vos passions.
        assertEquals("Rejoignez un groupe local pour rencontrer du monde, tester une nouvelle activité ou partager vos passions.",
                driver.findElement(By.cssSelector(".exploreHome-hero-subTitle span")).getText());

        // Un bouton d'inscription rouge doit être présent en haut de la page, juste après la punchline, il doit contenir le texte :
        // Rejoindre Meetup et doit contenir un lien qui me mène vers la page "https://www.meetup.com/fr-FR/register/"
        WebElement redButton = driver.findElement(By.cssSelector("a#joinMeetupButton"));
        assertEquals("Rejoindre Meetup", redButton.findElement(By.tagName("span")).getText());
        assertEquals("https://www.meetup.com/fr-FR/register/", redButton.getAttribute("href"));
    }

    @Test
    public void testSearch() {
        driver.get("https://www.meetup.com/fr-FR/find/outdoors-adventure/?allMeetups=false&radius=42&userFreeform=Paris&mcId=c1011740&mcName=Paris%2C+FR");

        // Le titre de la page d'accueil et du h1 contiennent tous les deux Nature et aventure
        assertTrue(driver.getTitle().contains("Nature et aventure"));
        assertTrue(driver.findElement(By.cssSelector("h1.text--display1")).getText().contains("Nature et aventure"));

        // La page de recherche contient un bandeau de recherche avec le champ de recherche, le rayon de recherche,
        // la ville de recherche, un choix d'affichage de la liste entre Groupe et Calendrier.
        WebElement searchBanner = driver.findElement(By.id("findNavBar"));
        assertFalse(searchBanner.findElements(By.cssSelector(".dropdown.callout")).isEmpty());
        assertFalse(searchBanner.findElements(By.cssSelector(".dropdown.callout.center > .dropdown-toggle")).isEmpty());
        assertFalse(searchBanner.findElements(By.cssSelector(".dropdown.callout.center.location-display")).isEmpty());
        assertFalse(searchBanner.findElements(By.cssSelector("ul#simple-view-selector > li + li")).isEmpty());

        // Le tri par défaut est le tri par pertinence.
        assertEquals("pertinence", driver.findElement(By.cssSelector("#simple-find-order .simple-order-copy")).getText());

        // Il y a 4 tri possibles: pertinence, plus récents, nombre de membres, proximité
        String[] sorts = driver.findElements(By.cssSelector("#simple-find-order ul.dropdown-menu li:not(.display-none)>a")).stream().map(el -> el.getAttribute("data-copy")).toArray(String[]::new);
        assertArrayEquals(new String[]{"pertinence", "plus récents", "nombre de membres", "proximité"}, sorts);

        // Quand je clique sur le choix d'affichage calendrier, la liste se met à jour et affiche des événements jour par jour ainsi qu'un calendrier.
        driver.findElement(By.id("simple-view-selector-event")).click();
        assertFalse(driver.findElements(By.className("event-listing-container")).isEmpty());
        assertFalse(driver.findElements(By.id("simple-date-selector")).isEmpty());

        //Quand je clique sur le 30 du mois courant du calendrier, le premier résultat de la liste qui s'affiche correspond à un événement du 30 du mois courant.
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)", "");
        driver.findElement(By.cssSelector(".date_30 a")).click();
        assertTrue(driver.findElement(By.className("date-indicator")).getText().contains("30"));
    }

    @Test
    public void testFiche() {
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/?_locale=fr-FR");

        // La fiche Meetup doit contenir un titre "cliquable" avec le nom du meetup, un lieu, le total des membres,
        // les organisateurs, un bouton rejoindre ce groupe, et une photo de présentation.
        assertFalse(driver.findElements(By.cssSelector(".groupHomeHeader h1.groupHomeHeader-groupName>a.groupHomeHeader-groupNameLink")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector(".groupHomeHeader ul.organizer-city a.groupHomeHeaderInfo-cityLink")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector(".groupHomeHeader a.groupHomeHeaderInfo-memberLink")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector(".groupHomeHeader a.orgInfo-name-superGroup")).isEmpty());

        // La fiche doit contenir un bandeau d'onglets avec "A propos", "Evénements", "Membres", "Photos", "Discussions", "Plus".
        String[] links = driver.findElements(By.cssSelector(".groupAnchorBar.stripe li>.link>span")).stream().map(WebElement::getText).toArray(String[]::new);
        assertArrayEquals(new String[]{"À propos", "Événements", "Membres", "Photos", "Discussions", "Plus"}, links);

        // On doit pouvoir cliquer sur les événements passés, les prochains événements, et pouvoir voir tous les membres, et toutes les photos d'un groupes.
        assertFalse(driver.findElements(By.cssSelector(".groupHome-eventsList-upcomingEvents a")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector(".groupHome-eventsList-pastEvents a")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector("a.groupMembers-memberListLink.link")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector("a.groupPhotos-photoListLink.link")).isEmpty());

        // Si aucun prochain meetup n'est prévu, un bandeau doit apparaitre à la place indiquant ce message en titre: Aucun événement à venir
        driver.get("https://www.meetup.com/fr-FR/BreakTheRules/");
        assertEquals("Aucun événement à venir", driver.findElement(By.cssSelector(".emptyEventCard p.text--secondary > span")).getText());

        // Si je veux rejoindre le groupe, je dois cliquer sur rejoindre puis entrer mes informations de membre et donc m'identifier via facebook ou google ou identifiant de site.
        // Sinon, je peux aussi m'inscrire et là je dois être rediriger vers /register/?method=email
        driver.findElement(By.id("actionButtonLink")).click();
        assertFalse(driver.findElements(By.cssSelector("a.signUpModal-facebook")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector("a.signUpModal-google")).isEmpty());
        assertFalse(driver.findElements(By.cssSelector("a.signUpModal-email")).isEmpty());

        driver.findElement(By.cssSelector("a.signUpModal-email")).click();
        assertTrue(driver.getCurrentUrl().startsWith("https://secure.meetup.com/fr-FR/register"));

        // Si j'ai une question, je dois pouvoir contacter l'organisateur depuis la fiche de membre.
        // En cliquant sur contacter je dois alors être automatiquement redirigé vers la page de connexion https://secure.meetup.com/fr-FR/login/
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/?_locale=fr-FR");

        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,200)", "");
        driver.findElement(By.cssSelector(".orgIntroCard-orgDetails a.orgInfo-message")).click();
        assertTrue(driver.getCurrentUrl().startsWith("https://secure.meetup.com/fr-FR/login"));
    }

    @Test
    public void testJobs() {
        driver.get("https://www.meetup.com/fr-FR/careers/");

        //La page carrière contient tout en haut la punchline: "Join our team, find your people"
        assertEquals("Join our team, find your people", driver.findElement(By.cssSelector("p.brandHero-slogan")).getText());

        // On dispose d'un bouton (call to action rouge) qui indique textuellement: "Explore Opportunities".
        assertEquals("Explore Opportunities", driver.findElement(By.cssSelector("a._brandHero-module_brandHeroButton__37glx")).getText());

        // Quand on clique sur le bouton explore opportunities, on arrive vers le milieu de la page et 9 thèmes d'offres d'emploi sont présent.
        // Juste en dessous, un lien "all opportunities" redirige vers la page "https://www.meetup.com/careers/all-opportunities"
        assertEquals(9, driver.findElements(By.cssSelector("._ourTeams-module_ourTeams-list__1p-ci ul > li")).size());
        WebElement link = driver.findElement(By.cssSelector("a._stayInTouch-module_clickable__3g3mA"));
        assertEquals("all opportunities", link.findElement(By.tagName("span")).getText());
        assertEquals("https://www.meetup.com/careers/all-opportunities", link.getAttribute("href"));

        // Une punchline en bas de page pour inciter à venir travailler chez Meetup avec les avantages !
        // Texte écris: Perks and benefits et en dessous 6 illustrations avec chacun un texte expliquant l'avantage
        assertEquals("Perks and benefits", driver.findElement(By.cssSelector(".perksAndBenefits-title > span")).getText());
        assertEquals(6, driver.findElements(By.cssSelector("ul._perksAndBenefits-module_perksAndBenefits-list__1ekNG > li")).size());
    }

}
