package com.todo.models;

/*
Паттерн Builder
Сила в том, что можно билдить сущность в любом порядке. Можно какие-то поля не указывать, они будут null.
Сеттеры в билдере возвращают саму сущность, а не void. Благодаря этому можно сделать цепочку вызовов (chain)
 */
public class TodoBuilder {
    private long id;
    private String text;
    private boolean completed;

    public TodoBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public TodoBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public TodoBuilder setCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    // Этот метод всегда должен быть в билдере и всегда называется build(), так принято
    public Todo build() {
        return new Todo(
                id, text, completed
        );
    }
}