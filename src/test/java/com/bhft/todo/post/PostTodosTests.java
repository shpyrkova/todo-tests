package com.bhft.todo.post;

import com.bhft.todo.BaseTest;
import com.todo.models.Todo;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;

public class PostTodosTests extends BaseTest {

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    public void testCreateTodoWithValidData() {
        Todo newTodo = new Todo(1, "New Task", false);
        // Отправляем POST запрос для создания нового TODO
        validAuthReq.create(newTodo);
        // Проверяем, что TODO было успешно создано
        List<Todo> todos = validAuthReq.readAll();
        // Ищем созданный TODO в списке
        Optional<Todo> createdTodo = todos
                .stream()
                .filter(todo -> todo.getId() == newTodo.getId())
                .findAny();
        // Assert
        Assertions.assertTrue(createdTodo.isPresent(), "Созданная задача не найдена в списке TODO");
        createdTodo.ifPresent(todo -> {
            Assertions.assertEquals(newTodo.getText(), todo.getText());
            Assertions.assertEquals(newTodo.isCompleted(), todo.isCompleted());
        });
    }

    /**
     * TC2: Попытка создания TODO с отсутствующими обязательными полями.
     */
    @Test
    public void testCreateTodoWithMissingFields() {
        // Создаем JSON без обязательного поля 'text'
        Todo invalidTodo = new Todo(4, null, true);
        todoRequest.create(invalidTodo)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(ContentType.TEXT)
                .body(notNullValue()); // Проверяем, что есть сообщение об ошибке
    }

    /**
     * TC3: Создание TODO с максимально допустимой длиной поля 'text'.
     */
    @Test
    public void testCreateTodoWithMaxLengthText() {
        // Предполагаем, что максимальная длина поля 'text' составляет 255 символов
        String maxLengthText = "A".repeat(255);
        Todo newTodo = new Todo(3, maxLengthText, false);
        // Отправляем POST запрос для создания нового TODO
        validAuthReq.create(newTodo);
        // Проверяем, что TODO было успешно создано
        List<Todo> todos = validAuthReq.readAll();
        // Ищем созданный TODO в списке
        Optional<Todo> createdTodo = todos
                .stream()
                .filter(todo -> todo.getId() == newTodo.getId())
                .findAny();
        // Assert
        Assertions.assertTrue(createdTodo.isPresent(), "Созданная задача не найдена в списке TODO");
        createdTodo.ifPresent(todo -> {
            Assertions.assertEquals(newTodo.getText(), todo.getText());
            Assertions.assertEquals(newTodo.isCompleted(), todo.isCompleted());
        });
    }

    /**
     * TC4: Передача некорректных типов данных в полях.
   Тест на попытку передать неверный тип данных относится к контрактному тестированию.
   Это не уровень функциональных API тестов.

    @Test
    public void testCreateTodoWithInvalidDataTypes() {
        // Поле 'completed' содержит строку вместо булевого значения
        Todo newTodo = new Todo(3, "djjdjd", false);

        todoRequest.create(newTodo)
                .then()
                .statusCode(400)
                .contentType(ContentType.TEXT)
                .body(notNullValue()); // Проверяем, что есть сообщение об ошибке
    }
     */

    /**
     * TC5: Создание TODO с уже существующим 'id' (если 'id' задается клиентом).
     */
    @Test
    public void testCreateTodoWithExistingId() {
        // Сначала создаем TODO с id = 5
        Todo firstTodo = new Todo(5, "First Task", false);
        todoRequest.create(firstTodo);
        // Пытаемся создать другую TODO с тем же id
        Todo duplicateTodo = new Todo(5, "Duplicate Task", true);
        todoRequest.create(duplicateTodo)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(notNullValue()); // Проверяем, что есть сообщение об ошибке
    }

}
