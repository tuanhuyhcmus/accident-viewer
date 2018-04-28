/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;

/**
 *
 * @author huynct
 */
public class JsonUtil {
    
    private static final Gson _gson = new Gson();
    
    public static JsonObject parseJsonObject(String data) {
        
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return (jsonEle.isJsonObject())?jsonEle.getAsJsonObject():null;
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static JsonObject parseJsonObject(String data, String objectName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject().getAsJsonObject(objectName);
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public static String parseStringValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsString();
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static String parseStringValue(String data, String objectName, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                    .getAsJsonObject(objectName)
                    .get(elementName)
                    .getAsString();
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public static Long parseLongValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsLong();
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static Long parseLongValue(String data, String objectName, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                    .getAsJsonObject(objectName)
                    .get(elementName)
                    .getAsLong();
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public static Integer parseIntValue(String data, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                .get(elementName)
                .getAsInt();
        } catch (Exception ex) {
            
        }
        return null;
    }
    
    public static Integer parseIntValue(String data, String objectName, String elementName) {
        try { 
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonEle = jsonParser.parse(data);
            return jsonEle.getAsJsonObject()
                    .getAsJsonObject(objectName)
                    .get(elementName)
                    .getAsInt();
        } catch (Exception ex) {
            
        }
        
        return null;
    }
    
    public static <T extends Object> T fromJson(JsonElement json, Class<T> classOfT) {
       
        try { 
            return _gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException ex) {
            
        }
        
        return null;
    }
    
    public static <T extends Object> T fromJson(JsonElement json, Type typeOfT) {
       
        try { 
            return _gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException ex) {
            
        }
        
        return null;
    }
    
    public static String toJson(JsonElement jsonElement) {
        return _gson.toJson(jsonElement);
    }
    
    public static JsonElement toJsonTree(Object src) {
        return _gson.toJsonTree(src);
    }
}
