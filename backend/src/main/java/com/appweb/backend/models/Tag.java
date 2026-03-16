package com.appweb.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    private Long id;

    private String name;

    private String slug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}