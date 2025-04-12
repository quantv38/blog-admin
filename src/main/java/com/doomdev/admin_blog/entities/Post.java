package com.doomdev.admin_blog.entities;
import com.doomdev.admin_blog.contants.enums.StatusPost;
import com.doomdev.admin_blog.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = DateUtils.ES_DATE_FORMAT)
    @Column(name = "post_date")
    private LocalDate postDate;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "slug", nullable = false)
    private String slug;

    @Lob
    @Column(name = "short_content")
    private String shortContent;

    @Lob
    @Column(name = "content")
    private String content;

    @ColumnDefault("255")
    @Column(name = "ordering", nullable = false)
    private Integer ordering;

    @Column(name = "status", nullable = false)
    private StatusPost status;

    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("1")
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}