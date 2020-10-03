package bggo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Common;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuyThread extends Thread {
    public  RemoteWebDriver driver =  null;
    public RemoteWebDriver sellDriver = null;
    private String exchangeUrl;
    private String acc1, acc2;
    private double countOfCoins;

    public BuyThread(RemoteWebDriver sellDriver, RemoteWebDriver driver, String exchangeUrl, double countOfCoins, String acc1, String acc2) {
        this.driver = driver;
        this.exchangeUrl = exchangeUrl;
        this.acc1 = acc1;
        this.acc2 = acc2;
        this.sellDriver = sellDriver;
        this.countOfCoins = countOfCoins;
    }

    @Override
    public void run(){
        driver.get(exchangeUrl);
        System.out.println("dsaaa");
        Wait<WebDriver> wait = new WebDriverWait(driver, 3600, 10000);
        String url = exchangeUrl.split("exchange/")[1];
        String f = url.split("/")[0], s = url.split("/")[1];
        String price = getPrice(f, s);
        price = price.split("\\.")[0] + "." + price.split("\\.")[1].substring(0, 8);
        driver.executeScript(String.format("document.getElementById('price19260817').value='%s'", price).replaceAll(",", "."));
        driver.executeScript("document.getElementById('amount19260817').value='" + countOfCoins + "';");
        SellThread sellThread = new SellThread(sellDriver, driver, price,String.valueOf(countOfCoins), acc1, acc2, exchangeUrl);
        sellThread.start();
        try {
            sellThread.join();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.run();
    }


    public String getPrice(String firstCurrency, String secondCurrency){
        int firstCurrencyId = 0;
        String listJson = Common.getRequest("https://api.coinmarketcap.com/v2/listings/");
        JsonObject listJsonBody = new JsonParser().parse(listJson).getAsJsonObject();
        JsonArray data = listJsonBody.getAsJsonArray("data");
        for(JsonElement el : data){
            JsonObject element = el.getAsJsonObject();
            String symb = element.get("symbol").getAsString();
            if(symb.equals(firstCurrency)){
                firstCurrencyId = element.get("id").getAsInt();
                break;
            }
        }
        String getCurrUrl = "https://api.coinmarketcap.com/v2/ticker/" + firstCurrencyId + "/?convert=" + secondCurrency;
        JsonObject resultCurrJsonObject = new JsonParser().parse(Common.getRequest(getCurrUrl)).getAsJsonObject();
        JsonObject resultJsonData = resultCurrJsonObject.getAsJsonObject("data");
        JsonObject quotes = resultJsonData.getAsJsonObject("quotes");
        return quotes.getAsJsonObject(secondCurrency).get("price").getAsString();
    }



}
