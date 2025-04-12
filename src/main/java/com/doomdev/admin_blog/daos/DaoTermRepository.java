package com.doomdev.admin_blog.daos;

import com.doomdev.admin_blog.configs.DBConfig;
import com.doomdev.admin_blog.contants.enums.Status;
import com.doomdev.admin_blog.entities.QTerm;
import com.doomdev.admin_blog.entities.Term;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoTermRepository {
    private final JPAQueryFactory queryFactory;

    public DaoTermRepository(@Qualifier(DBConfig.JPA_QUERY_FACTORY) JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Term> findAllTermActive() {
        QTerm term = QTerm.term;
        return queryFactory.selectFrom(term)
                .where(term.status.eq(Status.ACTIVE))
                .fetch();
    }

    public List<Term> findAllTermByIds(List<Long> ids) {
        QTerm term = QTerm.term;

        return queryFactory.selectFrom(term)
                .where(term.id.in(ids))
                .where(term.status.eq(Status.ACTIVE))
                .fetch();
    }
}
