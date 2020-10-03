package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Common {
    public static String getRequest(String url){
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
            StringBuilder builder = new StringBuilder();
            try {
                InputStream errorStream = connection.getErrorStream();
                int rc;
                char[] buffer = new char[256];
                InputStreamReader reader = new InputStreamReader(errorStream);
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
