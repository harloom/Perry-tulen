package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private final String id;

    private final String name;

    private final String base64Image;

    public Category(String id, String name, String base64Image) {
        this.id = id;
        this.name = name;
        this.base64Image = base64Image;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("base64Image", base64Image);
        return map;
    }

    public static Category fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        String id = (String) map.get("id");
        String name = (String) map.get("name");
        String base64Image = (String) map.get("base64Image");
        return new Category(
                id,name,base64Image
        );

    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", base64Image='" + base64Image + '\'' +
                '}';
    }
}
