package com.doomdev.admin_blog.entities;

import com.doomdev.admin_blog.contants.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_terms")
public class PostTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "term_id", nullable = false)
    private Long termId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "status", nullable = false)
    private Status status;

}