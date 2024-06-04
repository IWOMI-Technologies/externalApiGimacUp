package com.iwomi.External_api_cccNewUp.service;


import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ApiClient {
    public HttpResponse<?> get(String url, HashMap<String, Object> queryParams, Class<?> asClass) {
        Unirest.config().verifySsl(false);

        var fullPath = "http://localhost:8084/digitalbank" + url;
        var rest = Unirest.get(fullPath);
        queryParams.forEach(rest::queryString);

        System.out.println(":::::: FULL URL ::::::::" + fullPath);
        System.out.println(":::::: queryParams ::::::::" + queryParams);

        var result = rest.asObject(asClass);

        System.out.println(":::::: Result Status Code ::::::::" + result.getStatus());
        System.out.println(":::::: Result BODY ::::::::" + result.getBody());

        return result;
    }

    public HttpResponse<?> post(String url, HashMap<String, Object> queryParams, HashMap<String, Object> body) {
        Unirest.config().verifySsl(false);

        var fullPath = "http://localhost:8084/digitalbank" + url;

        System.out.println(":::::: FULL URL ::::::::" + fullPath);
        System.out.println(":::::: queryParams ::::::::" + queryParams);

        var rest = Unirest.post(fullPath)
                .header("Content-Type", "application/json")
                .body(body);

        var result = rest.asString();

        System.out.println(":::::: Result Status Code ::::::::" + result.getStatus());
        System.out.println(":::::: Result BODY ::::::::" + result.getBody());

        return result;
    }
}
