package com.appweb.backend.services;

import com.appweb.backend.dto.PostDTO;
import com.appweb.backend.models.Post;
import com.appweb.backend.models.Role;
import com.appweb.backend.models.User;
import com.appweb.backend.repositories.PostRepository;
import com.appweb.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // ✔️ CREATE
    public Post createPost(String title, String content, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setSlug(title.toLowerCase().replace(" ", "-"));
        post.setPublishedAt(LocalDateTime.now());
        post.setAuthor(user);

        return postRepository.save(post);
    }

    // ✔️ READ
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // ✔️ DELETE (con permisos)
    public void deletePost(Long id, String username) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = post.getAuthor().getUsername().equals(username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No autorizado");
        }

        postRepository.delete(post);
    }

    // ✔️ UPDATE (con permisos)
    public Post updatePost(Long id, PostDTO dto, String username) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isOwner = post.getAuthor().getUsername().equals(username);
        boolean isAdmin = user.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("No autorizado");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return postRepository.save(post);
    }
}