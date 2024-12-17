package com.bhft.todo.get;

import com.bhft.todo.BaseTest;
import com.todo.annotations.PrepareTodo;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.todo.models.Todo;

import java.util.List;

@Epic("TODO Management")
@Feature("Get Todos API")
public class GetTodosTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @Description("Получение пустого списка TODO, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> todos = validAuthReq.readAll();
        Assertions.assertTrue(todos.isEmpty());
    }

    @Test
    @Description("Получение списка TODO с существующими записями")
    public void testGetTodosWithExistingEntries() {
        // Предварительно создать несколько TODO
        Todo todo1 = new Todo(1, "Task 1", false);
        Todo todo2 = new Todo(2, "Task 2", true);

        /*
         Паттерн Фасад - используем todoRequester, из которого достаем нужный тип запроса,
         вместо того чтобы обращаться в каждом тесте к разным типам
         */
        todoRequester.getValidatedRequest().create(todo1);
        // пример без Фасада
        validAuthReq.create(todo2);

        List<Todo> todos = validAuthReq.readAll();

        Assertions.assertEquals(1, todos.getFirst().getId());
        Assertions.assertEquals("Task 1", todos.getFirst().getText());
        Assertions.assertFalse(todos.getFirst().isCompleted());

        Assertions.assertEquals(2, todos.getLast().getId());
        Assertions.assertEquals("Task 2", todos.getLast().getText());
        Assertions.assertTrue(todos.getLast().isCompleted());
    }

    // аннотация PrepareTodo выполняется раньше, чем @BeforeEach deleteAllTodos(), поэтому все подготовленные записи удаляются
    @Test
    @PrepareTodo(5)
    @Description("Использование параметров offset и limit для пагинации")
    public void testGetTodosWithOffsetAndLimit() {
        List<Todo> todos = validAuthReq.readAll(2, 2);
        Assertions.assertEquals(2, todos.size());

//  Для реализации проверок нужно передавать в аннотацию желаемые данные или доставать их оттуда
//        Assertions.assertEquals(3, todos.getFirst().getId());
//        Assertions.assertEquals("Task 3", todos.getFirst().getText());
//
//        Assertions.assertEquals(4, todos.getLast().getId());
//        Assertions.assertEquals("Task 4", todos.getLast().getText());
    }

    @Test
    @DisplayName("Передача некорректных значений в offset и limit")
    public void testGetTodosWithInvalidOffsetAndLimit() {
        todoRequest.readAll(-1, 2)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));

        /* Это контрактные тесты?
           Если они уместны здесь, то как передать невалидный тип данных в запросы TodoRequest?
        */

        // Тест с нечисловым limit
        given()
                .filter(new AllureRestAssured())
                .queryParam("offset", 0)
                .queryParam("limit", "abc")
                .when()
                .get("/todos")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));

        // Тест с отсутствующим значением offset
        given()
                .filter(new AllureRestAssured())
                .queryParam("offset", "")
                .queryParam("limit", 2)
                .when()
                .get("/todos")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType("text/plain")
                .body(containsString("Invalid query string"));
    }

    @Test
    @DisplayName("Проверка ответа при превышении максимально допустимого значения limit")
    public void testGetTodosWithExcessiveLimit() {
        // Создаем 10 TODO
        for (int i = 1; i <= 10; i++) {
            validAuthReq.create(new Todo(i, "Task " + i, i % 2 == 0));
        }
        List<Todo> todos = validAuthReq.readAll(0, 1000);
        // Проверяем, что вернулось 10 задач
        Assertions.assertEquals(10, todos.size());
    }
}
