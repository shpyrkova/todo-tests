package com.todo.specs.response;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

/*
Паттерн Strategy. Например, могут быть разные стратегии проверки ответа в тесте:
assertThat()
.strategy(#1)
assertThat()
.strategy(#2)
 */
public class IncorrectDataResponse {
    // strategy #1
    public ResponseSpecification sameId(long id) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody("message", Matchers.containsString("You are trying to use the same id:" + id) );
        return responseSpecBuilder.build();
    }

    // strategy #2
    // ....
}