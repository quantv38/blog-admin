package com.doomdev.admin_blog.services.post;

import com.doomdev.admin_blog.dtos.requests.CreatePostRequest;
import com.doomdev.admin_blog.dtos.requests.PostListRequest;
import com.doomdev.admin_blog.dtos.requests.UpdatePostRequest;
import com.doomdev.admin_blog.dtos.responses.CreatePostResponse;
import com.doomdev.admin_blog.dtos.responses.PostDetailResponse;
import com.doomdev.admin_blog.dtos.responses.PostListResponse;
import com.doomdev.admin_blog.responses.PageResponse;

import java.util.List;

public interface PostService {
    PageResponse<PostListResponse> getListPosts(PostListRequest request);

    PostDetailResponse getDetailPost(String slug);

    List<PostListResponse> getRelatedPosts(String slug);

    CreatePostResponse createPost(CreatePostRequest request);

    void updatePost(UpdatePostRequest request);
}
