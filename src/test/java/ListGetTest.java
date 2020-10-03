import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.DefaultProperty;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListGetTest {


    @RepeatedTest(100)
    public void parseUrl(){
        String price = getPrice();
        price = price.split("\\.")[0] + "." + price.split("\\.")[1].substring(0, 8);
        System.out.println(price);
    }

    public String getPrice(){
        String firstCurrency = "PRA", secondCurrency = "ETH";
        int firstCurrencyId = 0;
        String listJson = getRequest("https://api.coinmarketcap.com/v2/listings/");
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
        JsonObject resultCurrJsonObject = new JsonParser().parse(getRequest(getCurrUrl)).getAsJsonObject();
        JsonObject resultJsonData = resultCurrJsonObject.getAsJsonObject("data");
        JsonObject quotes = resultJsonData.getAsJsonObject("quotes");
        return quotes.getAsJsonObject(secondCurrency).get("price").getAsString();
    }
    private String getRequest(String url){
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            InputStream is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[256];
            int rc;

            StringBuilder sb = new StringBuilder();

            while ((rc = reader.read(buffer)) != -1)
                sb.append(buffer, 0, rc);

            reader.close();
            return sb.toString();
        }catch (Exception e){
            InputStream errorStream = connection.getErrorStream();
            int rc;
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[256];
            InputStreamReader reader = new InputStreamReader(errorStream);
            try {
                while ((rc = reader.read(buffer)) != -1)
                    builder.append(buffer, 0, rc);
            }catch (Exception e1){
                e1.printStackTrace();
                return "";
            }
            return builder.toString();
        }
    }
}
