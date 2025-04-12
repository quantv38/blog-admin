package com.doomdev.admin_blog.services.category;

import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.daos.DaoTermRepository;
import com.doomdev.admin_blog.dtos.requests.CreateCategoryRequest;
import com.doomdev.admin_blog.dtos.requests.UpdateCategoryRequest;
import com.doomdev.admin_blog.dtos.responses.CategoryResponse;
import com.doomdev.admin_blog.entities.Term;
import com.doomdev.admin_blog.exceptions.AppException;
import com.doomdev.admin_blog.repositories.TermRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final TermRepository termRepository;
    private final DaoTermRepository daoTermRepository;

    @Override
    public void createCategory(CreateCategoryRequest request) {
        try {
            String alias = buildAlias(request.getName());

            Term newTerm = Term.builder()
                    .name(request.getName())
                    .alias(alias)
                    .parentId(Optional.ofNullable(request.getParentId()).orElse(0L))
                    .type(2)
                    .ordering(Optional.ofNullable(request.getOrdering()).orElse(255))
                    .createdAt(LocalDateTime.now())
                    .createdBy(1)
                    .build();

            termRepository.save(newTerm);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.EXCEPTION_SAVE_CATEGORY);
        }
    }

    @Override
    public void updateCategory(UpdateCategoryRequest request) {
        try {
            Term termExist = termRepository.findById(request.getId()).orElse(null);
            if (termExist == null) {
                throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
            }

            termExist.setParentId(request.getParentId());
            termExist.setOrdering(request.getOrdering());
            termExist.setStatus(request.getStatus());

            termRepository.save(termExist);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.EXCEPTION_SAVE_CATEGORY);
        }
    }

    @Override
    public List<CategoryResponse> getCategories() {
        Map<Long, List<CategoryResponse>> allCategories = daoTermRepository.findAllTermActive().stream()
                .map((item) -> CategoryResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .alias(item.getAlias())
                        .parentId(item.getParentId())
                        .build())
                .collect(Collectors.groupingBy(CategoryResponse::getParentId));

        List<CategoryResponse> rootCategory = allCategories.get(0L);
        return mapSubCategories(rootCategory, allCategories);
    }

    private String buildAlias(String slug) {
        List<String> allAlias = termRepository.findAll().stream().map(Term::getAlias).toList();
        if (CollectionUtils.isEmpty(allAlias) || !allAlias.contains(slug)) {
            return slug;
        }

        int index = 1;
        while (allAlias.contains(slug)) {
            slug = slug + "-" + index;
            index++;
        }
        return slug;
    }

    public List<CategoryResponse> mapSubCategories(List<CategoryResponse> categories, Map<Long, List<CategoryResponse>> allCategoryMap) {
        if (Objects.isNull(categories) || categories.isEmpty()) {
            return List.of();
        }
        for (CategoryResponse category : categories) {
            List<CategoryResponse> subCategory = allCategoryMap.get(category.getId());

            if (Objects.isNull(subCategory)) {
                continue;
            }

            List<CategoryResponse> result = mapSubCategories(subCategory, allCategoryMap);

            category.setSubCategories(result);
        }

        return categories;
    }
}
