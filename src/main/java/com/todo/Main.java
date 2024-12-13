package com.todo;

import com.todo.requests.Request;
import com.todo.requests.TodoRequest;
import com.todo.specs.RequestSpec;

public class Main {
    public static void main(String[] args) {
        Request request = new TodoRequest(RequestSpec.authSpec());
    }
}
