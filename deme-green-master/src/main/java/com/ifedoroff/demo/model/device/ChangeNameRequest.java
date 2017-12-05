package com.ifedoroff.demo.model.device;

/**
 * Created by Rostik on 06.08.2017.
 */
public class ChangeNameRequest {
    private String name;
    private String id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
