package com.blogapi.model.dto;

public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private int postCount;

    public CategoryResponse(Long id, String name, String description, int postCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postCount = postCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPostCount() {
        return postCount;
    }
}
