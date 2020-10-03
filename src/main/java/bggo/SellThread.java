package bggo;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SellThread extends Thread {

    public RemoteWebDriver driver = null;
    private String unitPrice;
    String quanity, acc1, acc2, exchangeUrl;
    public RemoteWebDriver buyDriver = null;

    public SellThread(RemoteWebDriver myDriver, RemoteWebDriver buyDriver, String unitPrice, String quanity, String acc1, String acc2, String exchangeUrl) {
        this.buyDriver = buyDriver;
        this.unitPrice = unitPrice;
        this.quanity = quanity;
        this.acc1 = acc1;
        this.acc2 = acc2;
        this.exchangeUrl = exchangeUrl;
        this.driver = myDriver;
    }

    @Override
    public void run() {
        driver.get(exchangeUrl);
        driver.executeScript("document.getElementById('price19260818').value='" +unitPrice + "';");
        driver.executeScript("document.getElementById('amount19260818').value='" + quanity + "';");
        buyDriver.executeScript("document.querySelector(\"button[class='buy primary button spin']\").click();");
        try {
            Thread.sleep(1000);
            driver.executeScript("document.querySelectorAll(\"button[class='sell primary button spin']\")[1].click();");
            Thread.sleep(5000);
            MainClass.main(new String[]{acc1, acc2, exchangeUrl, quanity, "r"});
        }catch (Exception e){
            e.printStackTrace();
        }

        super.run();
    }
}
