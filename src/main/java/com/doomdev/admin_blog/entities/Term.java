package com.doomdev.admin_blog.entities;

import com.doomdev.admin_blog.contants.enums.Status;
import com.doomdev.admin_blog.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "terms")
public class Term {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "alias", nullable = false)
    private String alias;

    @Size(max = 255)
    @NotNull
    @ColumnDefault("''")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "type", nullable = false)
    private Integer type;

    @ColumnDefault("255")
    @Column(name = "ordering")
    private Integer ordering;

    @NotNull
    @Column(name = "status", nullable = false)
    private Status status;

    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @NotNull
    @JsonFormat(pattern = DateUtils.DATETIME_FORMAT)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

}