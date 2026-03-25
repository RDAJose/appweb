package com.appweb.backend.dto;

import java.time.LocalDateTime;

public class PostResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    private AuthorDTO author;

    public static class AuthorDTO {
        private Long id;
        private String username;
        private String email;

        public AuthorDTO(Long id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
    }

    public PostResponseDTO(Long id, String title, String content, String slug,
                           LocalDateTime createdAt, LocalDateTime publishedAt,
                           AuthorDTO author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.slug = slug;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.author = author;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSlug() { return slug; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public AuthorDTO getAuthor() { return author; }
}