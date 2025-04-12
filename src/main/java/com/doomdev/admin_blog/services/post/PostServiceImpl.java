package com.doomdev.admin_blog.services.post;

import com.doomdev.admin_blog.contants.enums.ImageType;
import com.doomdev.admin_blog.contants.enums.Status;
import com.doomdev.admin_blog.contants.enums.StatusPost;
import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.daos.DaoImageRepository;
import com.doomdev.admin_blog.daos.DaoPostRepository;
import com.doomdev.admin_blog.daos.DaoPostTermRepository;
import com.doomdev.admin_blog.daos.DaoTermRepository;
import com.doomdev.admin_blog.dtos.PostTermDto;
import com.doomdev.admin_blog.dtos.requests.CreatePostRequest;
import com.doomdev.admin_blog.dtos.requests.PostListRequest;
import com.doomdev.admin_blog.dtos.requests.UpdatePostRequest;
import com.doomdev.admin_blog.dtos.responses.CreatePostResponse;
import com.doomdev.admin_blog.dtos.responses.PostDetailResponse;
import com.doomdev.admin_blog.dtos.responses.PostListResponse;
import com.doomdev.admin_blog.entities.Image;
import com.doomdev.admin_blog.entities.Post;
import com.doomdev.admin_blog.entities.PostTerm;
import com.doomdev.admin_blog.entities.Term;
import com.doomdev.admin_blog.exceptions.AppException;
import com.doomdev.admin_blog.repositories.ImageRepository;
import com.doomdev.admin_blog.repositories.PostRepository;
import com.doomdev.admin_blog.repositories.PostTermRepository;
import com.doomdev.admin_blog.responses.PageResponse;
import com.doomdev.admin_blog.utils.StrUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final DaoPostRepository daoPostRepository;

    private final DaoPostTermRepository daoPostTermRepository;

    private final DaoImageRepository daoImageRepository;

    private final DaoTermRepository daoTermRepository;

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final PostTermRepository postTermRepository;

    @Override
    public PageResponse<PostListResponse> getListPosts(PostListRequest request) {
        Page<Post> posts = daoPostRepository.findPostByFilter(request);
        List<Long> postIds = posts.stream().map(Post::getId).toList();
        Map<Long, List<PostTerm>> postTermMap = daoPostTermRepository.getTermByListPostId(postIds).stream()
                .collect(Collectors.groupingBy(PostTerm::getPostId));
        Map<Long, Image> imageMap = daoImageRepository.findAllImageByListPostIdAndType(postIds, 1).stream()
                .collect(Collectors.toMap(Image::getPostId, Function.identity(), (a, b) -> b));
        Map<Long, Term> allTermActiveMap = daoTermRepository.findAllTermActive().stream()
                .collect(Collectors.toMap(Term::getId, Function.identity()));

        List<PostListResponse> postListResponses = posts.stream().map((post) -> {
            List<Long> termIds = Objects.nonNull(postTermMap.get(post.getId())) ? postTermMap.get(post.getId()).stream().map(PostTerm::getTermId).toList() : List.of();
            List<String> categories = new ArrayList<>();
            for (Long termId : termIds) {
                if (Objects.nonNull(allTermActiveMap.get(termId))) {
                    categories.add(allTermActiveMap.get(termId).getName());
                }
            }

            String imageThumbnail = Objects.nonNull(imageMap.get(post.getId())) ? imageMap.get(post.getId()).getLink() : null;

            return PostListResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .slug(post.getSlug())
                    .categories(categories)
                    .postDate(post.getPostDate  ())
                    .imageThumbnail(imageThumbnail)
                    .build();
        }).toList();

        return PageResponse.success(posts, postListResponses);
    }

    @Override
    public PostDetailResponse getDetailPost(String slug) {
        Post post = daoPostRepository.findPostBySlug(slug);

        if (Objects.isNull(post)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Image images = daoImageRepository.findAllImageByListPostIdAndType(List.of(post.getId()), 1).getFirst();
        List<String> categories = daoPostTermRepository.getTermByPostId(post.getId()).stream()
                .map(PostTermDto::getTermName).toList();

        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .postDate(post.getPostDate())
                .imageThumbnail(images.getLink())
                .content(post.getContent())
                .categories(categories)
                .build();
    }

    @Override
    public List<PostListResponse> getRelatedPosts(String slug) {
        Post post = daoPostRepository.findPostBySlug(slug);
        if (Objects.isNull(post)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        List<Long> termRelatedIds = daoPostTermRepository.getTermByListPostId(List.of(post.getId())).stream()
                .map(PostTerm::getTermId).toList();

        List<Long> postIds = daoPostTermRepository.getRelatedPostIdByTerm(termRelatedIds, post.getId());

        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }

        List<Post> relatedPosts = daoPostRepository.findPostByIds(postIds);

        Map<Long, List<PostTerm>> postTermMap = daoPostTermRepository.getTermByListPostId(postIds).stream()
                .collect(Collectors.groupingBy(PostTerm::getPostId));
        Map<Long, Image> imageMap = daoImageRepository.findAllImageByListPostIdAndType(postIds, 1).stream()
                .collect(Collectors.toMap(Image::getPostId, Function.identity(), (a, b) -> b));
        Map<Long, Term> allTermActiveMap = daoTermRepository.findAllTermActive().stream()
                .collect(Collectors.toMap(Term::getId, Function.identity()));

        return relatedPosts.stream().map((item) -> {
            List<Long> termIds = Objects.nonNull(postTermMap.get(item.getId())) ? postTermMap.get(item.getId()).stream().map(PostTerm::getTermId).toList() : List.of();
            List<String> categories = new ArrayList<>();
            for (Long termId : termIds) {
                if (Objects.nonNull(allTermActiveMap.get(termId))) {
                    categories.add(allTermActiveMap.get(termId).getName());
                }
            }

            String imageThumbnail = Objects.nonNull(imageMap.get(item.getId())) ? imageMap.get(item.getId()).getLink() : null;

            return PostListResponse.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .slug(item.getSlug())
                    .categories(categories)
                    .postDate(item.getPostDate())
                    .imageThumbnail(imageThumbnail)
                    .build();
        }).toList();
    }

    @Override
    @Transactional(transactionManager = "productTransactionManager")
    public CreatePostResponse createPost(CreatePostRequest request) {
        try {
            String slug = StrUtils.removeAccent(request.getTitle()).replaceAll(" ", "-").toLowerCase();
            String slugValid = buildSlug(slug);

            Post newPost = Post.builder()
                    .title(request.getTitle())
                    .slug(slugValid)
                    .postDate(LocalDate.now())
                    .content(request.getContent())
                    .ordering(255)
                    .status(StatusPost.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .createdBy(1)
                    .updatedAt(LocalDateTime.now())
                    .build();

            Post postSaved = postRepository.save(newPost);

            buildNewImage(null, request.getImageThumbnail(), postSaved.getId());

            if (Objects.nonNull(request.getCategoryIds()) && !request.getCategoryIds().isEmpty()) {
                buildNewPostTerms(new ArrayList<>(), request.getCategoryIds(), postSaved.getId());
            }

            return new CreatePostResponse(slugValid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.EXCEPTION_SAVE_POST);
        }
    }

    private String buildSlug(String slug) {
        List<String> allSlugLike = daoPostRepository.findAllSlugsLike(slug);
        if (CollectionUtils.isEmpty(allSlugLike) || !allSlugLike.contains(slug)) {
            return slug;
        }

        int index = 1;
        while (allSlugLike.contains(slug)) {
            slug = slug + "-" + index;
            index++;
        }
        return slug;
    }

    @Override
    @Transactional(transactionManager = "productTransactionManager")
    public void updatePost(UpdatePostRequest request) {
        try {
            Post post = daoPostRepository.findPostById(request.getId(), StatusPost.ACTIVE);
            if (Objects.isNull(post)) {
                throw new AppException(ErrorCode.POST_NOT_FOUND);
            }

            post.setTitle(request.getTitle());
            post.setContent(request.getContent());

            postRepository.save(post);

            Image image = daoImageRepository.findAllImageByListPostIdAndType(List.of(post.getId()), 1).getFirst();
            buildNewImage(image, request.getImageThumbnail(), post.getId());

            List<PostTerm> oldPostTerm = daoPostTermRepository.getTermByListPostId(List.of(post.getId()));
            buildNewPostTerms(oldPostTerm, request.getCategoryIds(), post.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.EXCEPTION_SAVE_POST);
        }
    }

    private void buildNewImage(Image oldImage, String imageThumbnail, Long postId) {
        Image newImage = null;
        if (Objects.nonNull(imageThumbnail)) {
            if (Objects.isNull(oldImage)) {
                newImage = Image.builder()
                        .postId(postId)
                        .link(imageThumbnail)
                        .type(ImageType.THUMBNAIL)
                        .status(Status.ACTIVE)
                        .build();
            } else if (!oldImage.getLink().equals(imageThumbnail)) {
                newImage = oldImage;
                newImage.setLink(imageThumbnail);
            }
        } else {
            if (Objects.nonNull(oldImage)) {
                newImage = oldImage;
                newImage.setStatus(Status.DELETED);
            }
        }

        if (Objects.nonNull(newImage)) {
            imageRepository.save(newImage);
        }
    }

    private void buildNewPostTerms(List<PostTerm> oldPostTerms, List<Long> newCategoryIds, Long postId) {
        Map<Long, PostTerm> oldCategoryMap = oldPostTerms.stream().collect(Collectors.toMap(PostTerm::getTermId, Function.identity(), (existing, replacement) -> existing));

        List<PostTerm> newPostTerms = new ArrayList<>();
        for (Long newCategoryId : newCategoryIds) {
            PostTerm postTerm = PostTerm.builder()
                    .postId(postId)
                    .termId(newCategoryId)
                    .status(Status.ACTIVE)
                    .build();
            if (Objects.nonNull(oldCategoryMap.get(newCategoryId))) {
                postTerm.setId(oldCategoryMap.get(newCategoryId).getId());
                oldPostTerms.remove(oldCategoryMap.get(newCategoryId));
            }

            newPostTerms.add(postTerm);
        }

        oldPostTerms.forEach(oldPostTerm -> {
            oldPostTerm.setStatus(Status.DELETED);
        });

        if (!CollectionUtils.isEmpty(newPostTerms)) {
            postTermRepository.saveAll(newPostTerms);
        }

        if (!CollectionUtils.isEmpty(oldPostTerms)) {
            postTermRepository.saveAll(oldPostTerms);
        }
    }
}
