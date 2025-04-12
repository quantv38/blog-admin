package com.doomdev.admin_blog.controllers;

import com.doomdev.admin_blog.dtos.requests.PostListRequest;
import com.doomdev.admin_blog.dtos.responses.PostDetailResponse;
import com.doomdev.admin_blog.dtos.responses.PostListResponse;
import com.doomdev.admin_blog.responses.DefaultListResponse;
import com.doomdev.admin_blog.responses.DefaultResponse;
import com.doomdev.admin_blog.responses.PageResponse;
import com.doomdev.admin_blog.services.post.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @GetMapping("/get-by-filter")
    public PageResponse<PostListResponse> getListPosts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize
    ) {
        PostListRequest request = PostListRequest.builder()
                .keyword(keyword)
                .categoryId(categoryId)
                .page(page)
                .pageSize(pageSize)
                .build();
        return postService.getListPosts(request);
    }

    @GetMapping("/detail")
    public DefaultResponse<PostDetailResponse> getDetailPost(@RequestParam("slug") String slug) {
        return DefaultResponse.success(postService.getDetailPost(slug));
    }

    @GetMapping("/related")
    public DefaultListResponse<PostListResponse> getRelatedPosts(@RequestParam("slug") String slug) {
        return DefaultListResponse.success(postService.getRelatedPosts(slug));
    }
}
