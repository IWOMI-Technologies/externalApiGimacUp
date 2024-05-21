package com.iwomi.External_api_cccNewUp.ussd.Core;

import com.iwomi.External_api_cccNewUp.model.Nomenclature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalResponse {
    public static ArrayList<Nomenclature> responseBuilder1(
            String message, HttpStatus httpStatus, String success, Object responseObject
    ) {
        ArrayList cr= new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("httpStatus", httpStatus);
        response.put("success", success);
        response.put("data", responseObject);
        cr.add(response);

        return cr;
    }

    public static ResponseEntity<Object> responseBuilder2(
            String message, HttpStatus httpStatus, int success, Object responseObject,
            Object othersObject
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("httpStatus", httpStatus);
        response.put("success", success);
        response.put("data", responseObject);
        response.put("others", othersObject);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static Map<String, Object> responseBuilder3(
            String message, String success, Map<String, Object> responseObject
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("success", success);
        response.put("data", responseObject.get("data"));


        return response;
    }

    public static ArrayList<Nomenclature> responseBuilder4(
            String message, String success, ArrayList<?> responseObject
    ) {
        ArrayList cr= new ArrayList<>();
        cr.add(message);
        cr.add(success);
        cr.addAll(responseObject);
        return cr;
    }


}
