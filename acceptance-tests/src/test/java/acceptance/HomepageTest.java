package acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
	features = {"./src/test/java/acceptance"}, // ou se situe votre fichier .feature
	plugin = {"pretty"}
	)
public class HomepageTest {

}