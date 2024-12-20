package com.todo.requests;

import com.todo.config.Configuration;

// Builder для эндпойнта, куда обращается тест, в зависимости от property "version" в конфигах
// Если у теста аннотация @Mobile, добавляется эндпойнт /mobile
public class Endpoint {
    private String endpoint;
    private String version;

    public Endpoint(String endpoint) {
        this.endpoint = endpoint;
        this.version = Configuration.getProperty("version");
    }

    public String build() {
        String finalEndpoint = endpoint;
        if (this.version != null) {
            finalEndpoint = finalEndpoint + "/" + version;
        }
        return finalEndpoint;
    }
}
