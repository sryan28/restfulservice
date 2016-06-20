package practice.com.restfulservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlowerJSONParser {

    public static List<Flower> parseFeed(String content) {
        try {
            JSONArray jsArray = new JSONArray(content);
            List<Flower> flowerList = new ArrayList<>();

            for(int i = 0; i < jsArray.length(); i++) {
                JSONObject jobj = jsArray.getJSONObject(i);
                Flower flower = new Flower();

                flower.setProductId(jobj.getInt("productId"));
                flower.setName(jobj.getString("name"));
                flower.setCategory(jobj.getString("category"));
                flower.setInstructions(jobj.getString("instructions"));
                flower.setPhoto(jobj.getString("photo"));
                flower.setPrice(jobj.getDouble("price"));

                flowerList.add(flower);
            }

            return flowerList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}