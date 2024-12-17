package com.bhft.todo;

import com.todo.requests.TodoRequest;
import com.todo.requests.TodoRequester;
import com.todo.requests.ValidatedTodoRequest;
import com.todo.specs.request.RequestSpec;
import com.todo.storages.TestDataStorage;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import com.todo.models.Todo;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;

public class BaseTest {
    protected TodoRequester todoRequester;

    @BeforeEach
    public void setupTest() {
        todoRequester = new TodoRequester(RequestSpec.authSpec());
    }

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    protected TodoRequest todoRequest = new TodoRequest(RequestSpec.authSpec());
    protected ValidatedTodoRequest validAuthReq = new ValidatedTodoRequest(RequestSpec.authSpec());
    protected TodoRequest unauthReq = new TodoRequest(RequestSpec.unauthSpec());
    protected TodoRequest invalidAuthReq = new TodoRequest(RequestSpec.invalidAuthSpec());


    protected void createTodo(Todo todo) {
        given()
                .contentType("application/json")
                .body(todo)
                .when()
                .post("/todos")
                .then()
                .statusCode(201);
    }

    protected void deleteAllTodos() {

        Todo[] todos = given()
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .extract()
                .as(Todo[].class);

        for (Todo todo : todos) {
            given()
                    .auth()
                    .preemptive()
                    .basic("admin", "admin")
                    .when()
                    .delete("/todos/" + todo.getId())
                    .then()
                    .statusCode(204);
        }
    }

    @AfterEach
    public void clean() {
        TestDataStorage.getInstance().getStorage()
                .forEach((k, v) ->
                        new TodoRequest(RequestSpec.authSpec())
                                .delete(k));
        TestDataStorage.getInstance().clean();
    }

}
