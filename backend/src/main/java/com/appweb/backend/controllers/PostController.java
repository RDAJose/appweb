package com.appweb.backend.controllers;

import com.appweb.backend.dto.PostDTO;
import com.appweb.backend.dto.PostResponseDTO;
import com.appweb.backend.models.Post;
import com.appweb.backend.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestBody PostDTO postDTO,
            Authentication authentication
    ) {
        String username = authentication.getName();

        Post post = postService.createPost(
                postDTO.getTitle(),
                postDTO.getContent(),
                username
        );

        return ResponseEntity.ok(mapToDTO(post));
    }

    @GetMapping
    public List<PostResponseDTO> getPosts() {
        return postService.getAllPosts()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPost(@PathVariable Long id) {
        return mapToDTO(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @RequestBody PostDTO postDTO,
            Authentication authentication
    ) {
        String username = authentication.getName();

        Post updatedPost = postService.updatePost(id, postDTO, username);

        return ResponseEntity.ok(mapToDTO(updatedPost));
    }

    private PostResponseDTO mapToDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getSlug(),
                post.getCreatedAt(),
                post.getPublishedAt(),
                new PostResponseDTO.AuthorDTO(
                        post.getAuthor().getId(),
                        post.getAuthor().getUsername(),
                        post.getAuthor().getEmail()
                )
        );
    }
}