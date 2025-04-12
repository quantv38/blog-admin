package com.doomdev.admin_blog.daos;

import com.doomdev.admin_blog.configs.DBConfig;
import com.doomdev.admin_blog.contants.enums.StatusPost;
import com.doomdev.admin_blog.dtos.requests.PostListRequest;
import com.doomdev.admin_blog.entities.Post;
import com.doomdev.admin_blog.entities.QPost;
import com.doomdev.admin_blog.entities.QPostTerm;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class DaoPostRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public DaoPostRepository(@Qualifier(DBConfig.JPA_QUERY_FACTORY) JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<Post> findPostByFilter(PostListRequest filter) {
        QPost post = QPost.post;
        QPostTerm postTerm = QPostTerm.postTerm;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(StatusPost.ACTIVE));
        if (Objects.nonNull(filter.getCategoryId())) {
            builder.and(postTerm.id.eq(filter.getCategoryId()));
        }
        if (Objects.nonNull(filter.getKeyword())) {
            builder.and(post.title.like("%" + filter.getKeyword() + "%"));
        }

        List<Post> posts = jpaQueryFactory.selectDistinct(post).from(post)
                .join(postTerm).on(post.id.eq(postTerm.postId))
                .where(builder)
                .orderBy(post.postDate.desc())
                .offset((long) filter.getPage() * filter.getPageSize())
                .limit(filter.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(post.countDistinct()).from(post)
                .join(postTerm).on(post.id.eq(postTerm.postId))
                .where(builder)
                .fetchOne();

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getPageSize());

        return new PageImpl<>(posts, pageable, Objects.nonNull(count) ? count : 0);
    }

    public Post findPostBySlug(String slug) {
        QPost post = QPost.post;

        return jpaQueryFactory.selectFrom(post)
                .where(post.slug.eq(slug))
                .where(post.status.eq(StatusPost.ACTIVE))
                .fetchOne();
    }

    public List<Post> findPostByIds(List<Long> ids) {
        QPost post = QPost.post;

        return jpaQueryFactory.selectFrom(post)
                .where(post.id.in(ids))
                .where(post.status.eq(StatusPost.ACTIVE))
                .fetch();
    }

    public List<String> findAllSlugsLike(String word) {
        QPost post = QPost.post;

        return jpaQueryFactory.select(post.slug)
                .from(post)
                .where(post.slug.like(word + "%"))
                .fetch();
    }

    public Post findPostById(Long id, StatusPost status) {
        QPost post = QPost.post;

        return jpaQueryFactory.selectFrom(post)
                .where(post.id.eq(id))
                .where(post.status.eq(status))
                .fetchOne();
    }
}
