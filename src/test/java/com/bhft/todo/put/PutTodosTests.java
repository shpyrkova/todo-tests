package com.bhft.todo.put;

import com.bhft.todo.BaseTest;
import com.todo.models.Todo;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PutTodosTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    /**
     * TC1: Обновление существующего TODO корректными данными.
     */
    @Test
    public void testUpdateExistingTodoWithValidData() {
        // Создаем TODO для обновления
        Todo originalTodo = new Todo(1, "Original Task", false);
        todoRequest.create(originalTodo);
        // Обновленные данные
        Todo updatedTodo = new Todo(1, "Updated Task", true);
        // Отправляем PUT запрос для обновления
        validAuthReq.update(updatedTodo.getId(), updatedTodo);
        // Проверяем, что данные были обновлены
        // Так как update ничего не возвращает, запрашиваем все TODO
        List<Todo> todos = validAuthReq.readAll();
        Assertions.assertEquals(updatedTodo.getText(), todos.getFirst().getText());
        Assertions.assertTrue(todos.getFirst().isCompleted());
    }

    /**
     * TC2: Попытка обновления TODO с несуществующим id.
     */
    @Test
    public void testUpdateNonExistentTodo() {
        // Обновленные данные для несуществующего TODO
        Todo todo = new Todo(999, "Non-existent Task", true);
        todoRequest.update(todo.getId(), todo)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    /*
     TC3: Обновление TODO с отсутствием обязательных полей.
     Это контрактный тест

    @Test
    public void testUpdateTodoWithMissingFields() {
        // Создаем TODO для обновления
        Todo originalTodo = new Todo(2, "Task to Update", false);
        createTodo(originalTodo);

        // Обновленные данные с отсутствующим полем 'text'
        String invalidTodoJson = "{ \"id\": 2, \"completed\": true }";

        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(invalidTodoJson)
                .when()
                .put("/todos/2")
                .then()
                .statusCode(401);
                //.contentType(ContentType.JSON)
                //.body("error", containsString("Missing required field 'text'"));
    }
     */

    /*
     TC4: Передача некорректных типов данных при обновлении.
     Это контрактный тест

    @Test
    public void testUpdateTodoWithInvalidDataTypes() {
        // Создаем TODO для обновления
        Todo originalTodo = new Todo(3, "Another Task", false);
        createTodo(originalTodo);

        // Обновленные данные с некорректным типом поля 'completed'
        String invalidTodoJson = "{ \"id\": 3, \"text\": \"Updated Task\", \"completed\": \"notBoolean\" }";

        given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(invalidTodoJson)
                .when()
                .put("/todos/3")
                .then()
                .statusCode(401);
    }
     */

    /**
     * TC5: Обновление TODO без изменения данных (передача тех же значений).
     */
    @Test
    public void testUpdateTodoWithoutChangingData() {
        // Создаем TODO для обновления
        Todo originalTodo = new Todo(4, "Task without Changes", false);
        validAuthReq.create(originalTodo);
        validAuthReq.update(originalTodo.getId(), originalTodo);
        List<Todo> todos = validAuthReq.readAll();
        Assertions.assertEquals(originalTodo.getText(), todos.getFirst().getText());
        Assertions.assertFalse(todos.getFirst().isCompleted());
    }
}
