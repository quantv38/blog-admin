package com.doomdev.admin_blog.services.category;

import com.doomdev.admin_blog.dtos.requests.CreateCategoryRequest;
import com.doomdev.admin_blog.dtos.requests.UpdateCategoryRequest;
import com.doomdev.admin_blog.dtos.responses.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void createCategory(CreateCategoryRequest request);

    void updateCategory(UpdateCategoryRequest request);

    List<CategoryResponse> getCategories();
}
