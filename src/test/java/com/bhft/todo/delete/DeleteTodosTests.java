package com.bhft.todo.delete;

import com.bhft.todo.BaseTest;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.todo.models.Todo;

import java.util.List;
import java.util.Optional;

public class DeleteTodosTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    /**
     * TC1: Успешное удаление существующего TODO с корректной авторизацией.
     */
    @Test
    public void testDeleteExistingTodoWithValidAuth() {
        // Создаем TODO для удаления
        Todo todo = new Todo(1, "Task to Delete", false);
        validAuthReq.create(todo);
        // Удаляем TODO
        validAuthReq.delete(todo.getId());
        // Получаем список всех TODO и проверяем, что удаленная задача отсутствует
        List<Todo> todos = validAuthReq.readAll();
        Optional<Todo> createdTodo = todos
                .stream()
                .filter(td -> td.getId() == todo.getId())
                .findAny();
        // Проверяем, что удаленная задача отсутствует в списке
        Assertions.assertFalse(createdTodo.isPresent(), "Созданная задача не удалена");
    }

    /**
     * TC2: Попытка удаления TODO без заголовка Authorization.
     */
    @Test
    public void testDeleteTodoWithoutAuthHeader() {
        // Создаем TODO для удаления
        Todo todo = new Todo(2, "Task to Delete", false);
        validAuthReq.create(todo);
        // Удаляем TODO
        unauthReq.delete(todo.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
        // Получаем список всех TODO и проверяем, что задача не удалена
        List<Todo> todos = validAuthReq.readAll();
        Optional<Todo> createdTodo = todos
                .stream()
                .filter(td -> td.getId() == todo.getId())
                .findAny();
        Assertions.assertTrue(createdTodo.isPresent(), "Задача удалена неавторизованным пользователем");
    }

    /**
     * TC3: Попытка удаления TODO с некорректными учетными данными.
     */
    @Test
    public void testDeleteTodoWithInvalidAuth() {
        // Создаем TODO для удаления
        Todo todo = new Todo(3, "Task to Delete", false);

        validAuthReq.create(todo);
        invalidAuthReq.delete(todo.getId())
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        // Получаем список всех TODO и проверяем, что задача не удалена
        List<Todo> todos = validAuthReq.readAll();
        Optional<Todo> createdTodo = todos
                .stream()
                .filter(td -> td.getId() == todo.getId())
                .findAny();

        // Проверяем, что удаленная задача отсутствует в списке
        Assertions.assertTrue(createdTodo.isPresent(), "Задача удалена неавторизованным пользователем");
    }

    /**
     * TC4: Удаление TODO с несуществующим id.
     */
    @Test
    public void testDeleteNonExistentTodo() {
        // Отправляем DELETE запрос для несуществующего TODO с корректной авторизацией

        todoRequest.delete(999)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    /*
     TC5: Попытка удаления с некорректным форматом id (например, строка вместо числа).
     Больше похоже на контрактный тест

    @Test
    public void testDeleteTodoWithInvalidIdFormat() {
        // Отправляем DELETE запрос с некорректным id
        given()
                .filter(new AllureRestAssured())
                .auth()
                .preemptive()
                .basic("admin", "admin")
                .when()
                .delete("/todos/invalidId")
                .then()
                .statusCode(404);
//                .contentType(ContentType.JSON)
//                .body("error", notNullValue());
    }

     */
}
