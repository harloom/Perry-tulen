package com.ferry.tulen.datasources.models;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private final String id;

    private final String name;

    private final String fileStorage;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileStorage() {
        return fileStorage;
    }

    public Category(String id, String name, String fileStorage) {
        this.id = id;
        this.name = name;
        this.fileStorage = fileStorage;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("fileStorage", fileStorage);
        return map;
    }

    public static Category fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        String id = (String) map.get("id");
        String name = (String) map.get("name");
        String fileStorage = (String) map.get("fileStorage");
        return new Category(
                id,name,fileStorage
        );

    }

    @Override
    public String toString() {
        return  name;
    }
}
