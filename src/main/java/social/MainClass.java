package social;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

public class MainClass {
    public static void main(String[] args) {
        try {
            String profileId = args[0];
            int mlaClientPort = 35000;
            System.out.println("SOMETHING!");
            URI url = new URIBuilder("http://localhost:" + mlaClientPort + "/api/v1/webdriver").build();
            DesiredCapabilities dc = new DesiredCapabilities();
            String uri = args[1];
            byte bytes[] = uri.getBytes("UTF-8");
            uri = new String(bytes, "UTF-8");
            System.out.println("MainClass URI - " + uri);
            dc.setCapability("multiloginapp-profileId", profileId);
            RemoteWebDriver driver = new RemoteWebDriver(url.toURL(), dc);
            driver.get("http://example.com/?data=" + uri);
            Wait<WebDriver> wait = new WebDriverWait(driver, 3600, 1000);
            Function<? super WebDriver, Object> finish = (ExpectedCondition<Object>) webDriver -> {
                String driverUrl = webDriver.getCurrentUrl();
                return   driverUrl.equals("http://example.com/?data=complete");
            };
            wait.until(finish);
            driver.quit();
        }catch (Exception e){

        }
    }
}
