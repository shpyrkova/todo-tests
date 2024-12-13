package com.todo.requests;

import com.todo.models.Todo;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;

public class ValidatedToDoRequest extends Request implements CrudInterface<Todo>, SearchInterface<Todo> {

    private final TodoRequest todoRequest;

    public ValidatedToDoRequest(RequestSpecification reqSpec) {
        super(reqSpec);
        todoRequest = new TodoRequest(reqSpec);
    }

    public String create(Todo entity) {
        return todoRequest.create(entity)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED).extract().asString();
    }

    @Override
    public ValidatableResponse update(long id, Todo entity) {
        return todoRequest.update(id, entity)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public String delete(long id) {
        return todoRequest.delete(id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT).extract().asString();
    }

    @Override
    public List<Todo> readAll(int offset, int limit) {
        Todo[] todos = todoRequest.readAll(offset, limit)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK).extract().as(Todo[].class);
        return List.of(todos);
    }

    @Override
    public List<Todo> readAll() {
        Todo[] todos = todoRequest.readAll()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK).extract().as(Todo[].class);
        return List.of(todos);
    }

}
