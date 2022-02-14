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

            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--remote-debugging-port=9222");
            options.setBinary("/app/.apt/usr/bin/google-chrome");
            options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.addArguments("--headless");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("window-size=1800x900");
            options.addArguments("--disable-dev-shm-usage");
            String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36";
            options.addArguments("--user-agent=" + userAgent);
            driver = new ChromeDriver(options);
        }
        return driver;
    }
}
