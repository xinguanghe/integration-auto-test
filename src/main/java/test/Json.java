package test;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Json {
    /**
     * 解析Json内容
     * @return JsonValue 返回JsonString中JsonId对应的Value
     **/
    public static String getJsonValue(String JsonString, String JsonId) {
        String JsonValue = "";
        if (JsonString == null || JsonString.trim().length() < 1) {
            return null;
        }
        try {
            JSONObject obj1 = new JSONObject(JsonString);
            JsonValue = (String) obj1.getString(JsonId);
        } catch (JSONException e) {
            System.out.println("\ngetJsonValue========>JsonId:[" + JsonId +"]不存在！\n");
        }
        return JsonValue.trim();
    }
    public static JSONArray getNames(String JsonString) {
        JSONArray names = null;
        if (JsonString == null || JsonString.trim().length() < 1) {
            return null;
        }
        try {
            JSONObject obj1 = new JSONObject(JsonString);
            names = obj1.names();
        } catch (JSONException e) {
            System.out.println("\ngetNames========>JsonString非法: " + JsonString);
        }
        return names;
    }
}