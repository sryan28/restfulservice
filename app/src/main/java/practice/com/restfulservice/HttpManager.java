package practice.com.restfulservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpManager {
    public static String getdata(String uri) {
        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            con.disconnect();
            return sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
//                    return null;
                }
            }
        }
    }

}