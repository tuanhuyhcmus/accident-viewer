/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thesis.api.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author huynct
 */
public class CommonModel {
    
    public static final byte HEADER_HTML = 0;
    public static final byte HEADER_JS = 1;
    public static final byte HEADER_TEXT_PLAIN = 2;
    
    public static final String KEY_USER_SESSION = "key_user_session";
    private static final Gson _gson = new Gson();
    
    public static void prepareHeader(HttpServletResponse resp, byte type) {
        resp.setCharacterEncoding("utf-8");
        if (type == HEADER_HTML) {
            resp.setContentType("text/html; charset=utf-8");
        } else if (type == HEADER_JS) {
            resp.setContentType("text/javascript; charset=utf-8");
        } else if (type == HEADER_TEXT_PLAIN) {
            resp.setContentType("text/plain; charset=utf-8");
        }
        String appName = Config.getParam("static", "app_name");
        resp.addHeader("Server", appName);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setHeader("Access-Control-Max-Age", "86400");
    }

    public static void out(String content, HttpServletResponse respon) throws IOException {
        PrintWriter out = respon.getWriter();
        out.print(content);
    }
    
    public static String FormatResponse(int error, String msg) {
                
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        
        return _gson.toJson(json);
    }
    
    public static String FormatResponse(int error, String msg, Object objData) {
        
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        json.add("dt", _gson.toJsonTree(objData));
        
        return _gson.toJson(json);
    }
    
    public static String FormatResponse(int error, String msg, String objName, Object objData) {
        
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        JsonObject jsonSub = new JsonObject();
        jsonSub.add(objName, _gson.toJsonTree(objData));
        json.add("dt", jsonSub);
        
        return _gson.toJson(json);
    }
    
    public static String FormatResponse(int error, String msg, JsonElement jsonEle) {
        
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
       
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        json.add("dt", jsonEle);
        return _gson.toJson(json);
    }
    
    public static String FormatResponse(int error, String msg, String objName, JsonElement jsonEle) {
        
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
       
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        JsonObject jsonSub = new JsonObject();
        jsonSub.add(objName, jsonEle);
        json.add("dt", jsonSub);
        return _gson.toJson(json);
    }
    
     public static String FormatResponse(int error, String msg, String objName1, Object objData1, String objName2, Object objData2) {
        
        if (error == 0 && msg.equals("")) {
            msg = "No error";
        }
        
        JsonObject json = new JsonObject();
        json.addProperty("err", error);
        json.addProperty("msg", msg);
        JsonObject jsonParent = new JsonObject();
        jsonParent.add(objName1, _gson.toJsonTree(objData1));
        jsonParent.add(objName2, _gson.toJsonTree(objData2));
        json.add("dt", jsonParent);
        
        return _gson.toJson(json);
    }
     
    public static String FormatResponseEx(int msgType, String msgName, JsonElement jsonEle) {
        JsonObject json = new JsonObject();
        json.addProperty("type", msgType);
        json.addProperty("msg", msgName);
        if (jsonEle != null) {
            json.add("dt", jsonEle);
        }
        
        return _gson.toJson(json);
    }
}
