package practice.com.restfulservice;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpManager2 {
    public static String getdata(RequestPackage p) {
        BufferedReader reader = null;
        String uri = p.getUri();

        //GET
        if(p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();

        }

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());
            con.connect();

            JSONObject json = new JSONObject(p.getParams());
            String params = "params=" + json.toString();

            //POST/JSON
            if(p.getMethod().equals("POST")) {
                //output content to body of request
                con.setDoOutput(true);
                //write to output
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
//                writer.write(p.getEncodedParams());
                writer.write(params);

                writer.flush();
            }

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
                    return null;
                }
            }
        }
    }

}