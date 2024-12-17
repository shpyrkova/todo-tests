package com.todo.specs.request;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class RequestSpec {
    private RequestSpecBuilder requestSpecBuilder;

    private static RequestSpecBuilder baseSpecBuilder() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.addFilters(List.of(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()));
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.setAccept(ContentType.JSON);
        return requestSpecBuilder;
    }

    public static RequestSpecification unauthSpec() {
        return baseSpecBuilder().build();
    }

    public static RequestSpecification authSpec() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("admin");
        RequestSpecBuilder requestSpecBuilder = baseSpecBuilder();
        requestSpecBuilder.setAuth(authScheme);
        return requestSpecBuilder.build();
    }

    public static RequestSpecification invalidAuthSpec() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("password");
        RequestSpecBuilder requestSpecBuilder = baseSpecBuilder();
        requestSpecBuilder.setAuth(authScheme);
        return requestSpecBuilder.build();
    }

}
