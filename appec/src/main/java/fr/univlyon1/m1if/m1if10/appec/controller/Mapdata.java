package fr.univlyon1.m1if.m1if10.appec.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Extract form data from a request body.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class Mapdata {
    public static Map<String, String> extractFormData(String requestBody) {
        Map<String, String> formData = new HashMap<>();
        String[] pairs = requestBody.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                try {
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (Exception e) {
                    // Ignore or handle the exception
                }
                formData.put(key, value);
            }
        }
        return formData;
    }
}
