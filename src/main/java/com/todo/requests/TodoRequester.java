package com.todo.requests;

import io.restassured.specification.RequestSpecification;

/*
Паттерн Фасад
За фасадом скрываются разные варианты запросов
 */
public class TodoRequester {
    private final TodoRequest request;
    private final ValidatedTodoRequest validatedRequest;

    public TodoRequester(RequestSpecification requestSpecification) {
        // логика, которая прячется за фасадом
        this.request = new TodoRequest(requestSpecification);
        this.validatedRequest = new ValidatedTodoRequest(requestSpecification);
    }

    public TodoRequest getRequest() {
        return request;
    }

    public ValidatedTodoRequest getValidatedRequest() {
        return validatedRequest;
    }
}