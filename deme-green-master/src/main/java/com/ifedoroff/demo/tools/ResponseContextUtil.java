package com.ifedoroff.demo.tools;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Rostik on 29.07.2017.
 */
public class ResponseContextUtil {

    public static String buildInitContext()
    {
        JSONObject jsonObject = new JSONObject();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        jsonObject.append("userName",auth.getName());
        String jsonStr = jsonObject.toString();
        return jsonStr;
    }

}
