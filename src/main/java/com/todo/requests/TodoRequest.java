package com.todo.requests;

import com.todo.models.Todo;
import com.todo.requests.interfaces.CrudInterface;
import com.todo.requests.interfaces.SearchInterface;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TodoRequest extends Request implements CrudInterface<Todo>, SearchInterface<Todo> {
    private static final String TODO_ENDPOINT = "/todos/";

    public TodoRequest(RequestSpecification reqSpec) {
        super(reqSpec);
    }

    @Override
    public Response create(Todo entity) {
        return given()
                .spec(reqSpec)
                .body(entity)
                .when()
                .post(new Endpoint(TODO_ENDPOINT).build());
    }

    @Override
    public Response update(long id, Todo entity) {
        return given()
                .spec(reqSpec)
                .body(entity)
                .when()
                .put(new Endpoint(TODO_ENDPOINT).build() + id);
    }

    @Override
    public Response delete(long id) {
        return given()
                .spec(reqSpec)
                .when()
                .delete(new Endpoint(TODO_ENDPOINT).build() + id);
    }

    @Override
    public Response readAll(int offset, int limit) {
        return given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(new Endpoint(TODO_ENDPOINT).build());
    }

    @Override
    public Response readAll() {
        return given()
                .when()
                .get(new Endpoint(TODO_ENDPOINT).build());
    }
}
