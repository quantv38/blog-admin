package com.doomdev.admin_blog.controllers;

import com.doomdev.admin_blog.dtos.requests.CreateCategoryRequest;
import com.doomdev.admin_blog.dtos.requests.CreatePostRequest;
import com.doomdev.admin_blog.dtos.requests.UpdateCategoryRequest;
import com.doomdev.admin_blog.dtos.requests.UpdatePostRequest;
import com.doomdev.admin_blog.dtos.responses.CategoryResponse;
import com.doomdev.admin_blog.dtos.responses.CreatePostResponse;
import com.doomdev.admin_blog.dtos.responses.UploadImageResponse;
import com.doomdev.admin_blog.responses.DefaultListResponse;
import com.doomdev.admin_blog.responses.DefaultResponse;
import com.doomdev.admin_blog.services.category.CategoryService;
import com.doomdev.admin_blog.services.image.ImageService;
import com.doomdev.admin_blog.services.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
@RequiredArgsConstructor
public class AdminController {
    private final PostService postService;

    private final CategoryService categoryService;

    private final ImageService imageService;

    @PostMapping("/images/upload")
    public DefaultResponse<UploadImageResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        return DefaultResponse.success(imageService.uploadImage(file));
    }

    @PostMapping("/posts/create")
    public DefaultResponse<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        return DefaultResponse.success(postService.createPost(request));
    }

    @PostMapping("/posts/update")
    public DefaultResponse<List<String>> updatePost(@Valid @RequestBody UpdatePostRequest request) {
        postService.updatePost(request);
        return DefaultResponse.success(List.of());
    }

    @GetMapping("/categories/view-tree")
    public DefaultListResponse<CategoryResponse> getInfoTreeCategory() {
        return DefaultListResponse.success(categoryService.getCategories());
    }

    @PostMapping("/categories/create")
    public DefaultListResponse<List<String>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        categoryService.createCategory(request);
        return DefaultListResponse.success(List.of());
    }

    @PostMapping("/categories/update")
    public DefaultListResponse<List<String>> updateCategory(@Valid @RequestBody UpdateCategoryRequest request) {
        categoryService.updateCategory(request);
        return DefaultListResponse.success(List.of());
    }
}
