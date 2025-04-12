package com.doomdev.admin_blog.daos;

import com.doomdev.admin_blog.configs.DBConfig;
import com.doomdev.admin_blog.contants.enums.ImageType;
import com.doomdev.admin_blog.contants.enums.Status;
import com.doomdev.admin_blog.entities.Image;
import com.doomdev.admin_blog.entities.QImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DaoImageRepository {
    private final JPAQueryFactory queryFactory;

    public DaoImageRepository(@Qualifier(DBConfig.JPA_QUERY_FACTORY) JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<Image> findAllImageByListPostIdAndType(List<Long> postIds, Integer type) {
        QImage image = QImage.image;

        return queryFactory.selectFrom(image)
                .where(image.postId.in(postIds))
                .where(image.status.eq(Status.ACTIVE))
                .where(image.type.eq(ImageType.THUMBNAIL))
                .fetch();
    }
}
