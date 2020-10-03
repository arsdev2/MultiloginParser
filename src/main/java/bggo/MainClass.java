package bggo;

import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class MainClass {
    private static int count = 0;


    public static void main(String[] args) throws Exception{
        LinkedHashMap<String, String> accounts = new LinkedHashMap<>();
        accounts.put("sell",args[0]);
        accounts.put("buy", args[1]);
        String exchangeUrl = args[2];
        if(args.length > 4 && args[4].equals("r")){
            System.out.println("-------------LOGS--------------");
            accounts.put("sell",args[1]);
            accounts.put("buy", args[0]);
            for(Map.Entry<String, String> el : accounts.entrySet()){
                System.out.println(el.getKey() + "::::::" + el.getValue());
            }
            System.out.println("-------------END_LOGS-------------------");
        }
        RemoteWebDriver sell = null;
        for(Map.Entry<String, String> account : accounts.entrySet()) {
            String profileId = account.getValue();
            int mlaClientPort = 35000;
            URI url = new URIBuilder("http://localhost:" + mlaClientPort + "/api/v1/webdriver").build();
            DesiredCapabilities dc = new DesiredCapabilities();
            System.out.println("aaaaaa");
            dc.setCapability("multiloginapp-profileId", profileId);
            RemoteWebDriver driver = new RemoteWebDriver(url.toURL(), dc);
            String type = account.getKey();
            System.out.println("slsl");
            switch (type){
                case "buy":
                    BuyThread thread;
                    if(args.length > 4 && args[4].equals("r")) {
                        thread = new BuyThread(sell, driver, exchangeUrl,Double.parseDouble(args[3]),  args[1], args[0]);
                    }else{
                        thread = new BuyThread(sell, driver, exchangeUrl,Double.parseDouble(args[3]),  args[0], args[1]);
                    }
                    thread.start();

                    System.out.println("asaaaa");
                    break;
                case "sell":
                    sell = driver;
                    break;
            }
        }
    }

}
