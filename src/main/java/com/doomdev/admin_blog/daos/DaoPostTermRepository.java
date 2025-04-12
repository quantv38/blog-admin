package com.doomdev.admin_blog.daos;

import com.doomdev.admin_blog.configs.DBConfig;
import com.doomdev.admin_blog.contants.enums.Status;
import com.doomdev.admin_blog.contants.enums.StatusPost;
import com.doomdev.admin_blog.dtos.PostTermDto;
import com.doomdev.admin_blog.entities.PostTerm;
import com.doomdev.admin_blog.entities.QPost;
import com.doomdev.admin_blog.entities.QPostTerm;
import com.doomdev.admin_blog.entities.QTerm;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoPostTermRepository {
    private final JPAQueryFactory queryFactory;

    public DaoPostTermRepository(@Qualifier(DBConfig.JPA_QUERY_FACTORY) JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    public List<PostTerm> getTermByListPostId(List<Long> postIds) {
        QPostTerm postTerm = QPostTerm.postTerm;

        return queryFactory.selectFrom(postTerm)
                .where(postTerm.status.eq(Status.ACTIVE))
                .where(postTerm.postId.in(postIds))
                .fetch();
    }

    public List<PostTermDto> getTermByPostId(Long postId) {
        QPostTerm postTerm = QPostTerm.postTerm;
        QTerm term = QTerm.term;

        return queryFactory.select(Projections.constructor(PostTermDto.class, postTerm.id, postTerm.postId, postTerm.termId, term.name.as("termName"), term.alias.as("termAlias"), term.type.as("termType")))
                .from(postTerm)
                .join(term).on(postTerm.termId.eq(term.id))
                .where(postTerm.postId.eq(postId))
                .where(postTerm.status.eq(Status.ACTIVE))
                .where(term.status.eq(Status.ACTIVE))
                .fetch();
    }

    public List<Long> getRelatedPostIdByTerm(List<Long> termIds, Long postId) {
        QPostTerm postTerm = QPostTerm.postTerm;
        QPost post = QPost.post;

        return queryFactory.select(postTerm.postId)
                .from(postTerm)
                .join(post).on(postTerm.postId.eq(post.id))
                .where(postTerm.termId.in(termIds))
                .where(postTerm.status.eq(Status.ACTIVE))
                .where(post.status.eq(StatusPost.ACTIVE))
                .orderBy(post.postDate.desc())
                .limit(5)
                .fetch();
    }
}
