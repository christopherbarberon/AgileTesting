package functional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.FileNotFoundException;

public class DriverResolver {

    private static final String ext = System.getProperty("os.name").toLowerCase().contains("win") ? ".exe" : "";

    static WebDriver find() {
        File gecko = searchFor("geckodriver");
        if (gecko != null) {
            System.setProperty("webdriver.gecko.driver", gecko.getAbsolutePath());
            return new FirefoxDriver();
        }

        File chrome = searchFor("chromedriver");
        if (chrome != null) {
            System.setProperty("webdriver.chrome.driver", chrome.getAbsolutePath());
            return new ChromeDriver();
        }

        throw new IllegalStateException("Impossible de trouver un chomedriver ou geckodriver");
    }

    private static File searchFor(String name) {
        File file = new File("../drivers/" + name + ext);
        if (file.isFile())
            return file;

        file = new File("/Library/Java/JUNIT/" + name + ext);
        if (file.isFile())
            return file;

        return null;
    }

}
