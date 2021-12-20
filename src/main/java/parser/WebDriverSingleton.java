package parser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

public class WebDriverSingleton {

    public static WebDriver driver;

    public static WebDriver getInstance() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();

            //options.addArguments("--disable-gpu");
            //options.addArguments("--no-sandbox");
            //options.addArguments("--remote-debugging-port=9222");
            options.setBinary("/app/.apt/usr/bin/google-chrome");
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            //options.addArguments("--headless");

            driver = new ChromeDriver(options);
        }
        return driver;
    }
}